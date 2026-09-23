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
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MusicFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: MediaStoreRepository

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

        setupTabs()
        loadAudioTracks()

        return view
    }

    private fun setupTabs() {
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Tracks"))
        tabLayout.addTab(tabLayout.newTab().setText("Albums"))
        tabLayout.addTab(tabLayout.newTab().setText("Artists"))
        tabLayout.addTab(tabLayout.newTab().setText("Folders"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                loadAudioTracks()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadAudioTracks() {
        val trackList = repository.getAllAudioTracks()
        val adapter = AudioAdapter(trackList) { audioItem ->
            val intent = Intent(requireContext(), MusicPlayerActivity::class.java).apply {
                putExtra("SONG_TITLE", audioItem.title)
                putExtra("ARTIST_NAME", audioItem.artist)
                putExtra("AUDIO_URI", audioItem.uri.toString())
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
