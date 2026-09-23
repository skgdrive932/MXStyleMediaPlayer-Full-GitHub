package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.skkaushal.mxstyleplayer.model.AudioItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MusicFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: MediaStoreRepository
    private lateinit var adapter: AudioAdapter

    private var currentTabPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        tabLayout = view.findViewById(R.id.musicTabLayout)
        recyclerView = view.findViewById(R.id.recyclerViewMusic)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        repository = MediaStoreRepository(requireContext())

        setupAdapter()
        setupTabs()

        return view
    }

    private fun setupAdapter() {
        adapter = AudioAdapter(emptyList()) { item, position ->
            if (currentTabPosition == 0) {
                // Tracks Tab: Song Play activity open karein
                val intent = Intent(requireContext(), MusicPlayerActivity::class.java).apply {
                    putExtra("SONG_ID", item.id)
                    putExtra("SONG_PATH", item.dataPath)
                    putExtra("SONG_TITLE", item.title)
                    putExtra("SONG_ARTIST", item.artist)
                }
                startActivity(intent)
            } else {
                // Albums, Artists, Folders par click event handling (Folder detail/Song list view)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun setupTabs() {
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Tracks"))
        tabLayout.addTab(tabLayout.newTab().setText("Albums"))
        tabLayout.addTab(tabLayout.newTab().setText("Artists"))
        tabLayout.addTab(tabLayout.newTab().setText("Folders"))

        // Default Load Tracks
        loadTabData(0)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    currentTabPosition = position
                    loadTabData(position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadTabData(tabPosition: Int) {
        val itemsList: List<AudioItem> = when (tabPosition) {
            0 -> repository.getAllAudioTracks()
            1 -> repository.getAlbums()
            2 -> repository.getArtists()
            3 -> repository.getFolders()
            else -> repository.getAllAudioTracks()
        }

        adapter.updateList(itemsList)
    }
}
