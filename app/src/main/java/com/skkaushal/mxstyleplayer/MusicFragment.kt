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
import com.skkaushal.mxstyleplayer.util.AudioRepository

class MusicFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tabLayout: TabLayout
    private lateinit var repository: AudioRepository
    private lateinit var adapter: AudioAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        val rvId = resources.getIdentifier("recyclerViewMusic", "id", requireContext().packageName)
        val fallbackRvId = resources.getIdentifier("recyclerView", "id", requireContext().packageName)
        
        recyclerView = when {
            rvId != 0 -> view.findViewById(rvId)
            fallbackRvId != 0 -> view.findViewById(fallbackRvId)
            else -> view.findViewById(android.R.id.list)
        }

        val tabId = resources.getIdentifier("tabLayout", "id", requireContext().packageName)
        if (tabId != 0) {
            tabLayout = view.findViewById(tabId)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        repository = AudioRepository(requireContext())

        val initialList = repository.getAllAudioTracks()
        adapter = AudioAdapter(initialList) { selectedItem, _ ->
            val intent = Intent(requireContext(), MusicPlayerActivity::class.java).apply {
                putExtra("SONG_PATH", selectedItem.path)
                putExtra("SONG_TITLE", selectedItem.title)
                putExtra("SONG_ARTIST", selectedItem.artist)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> adapter.updateList(repository.getAllAudioTracks())
                    1 -> adapter.updateList(repository.getAlbums())
                    2 -> adapter.updateList(repository.getArtists())
                    3 -> adapter.updateList(repository.getFolders())
                    else -> adapter.updateList(repository.getAllAudioTracks())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view
    }
}
