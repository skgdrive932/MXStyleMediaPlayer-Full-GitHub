package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class VideoFoldersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: MediaStoreRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_folders, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewFolders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        repository = MediaStoreRepository(requireContext())
        loadFolders()

        return view
    }

    private fun loadFolders() {
        val folderList = repository.getAllFolders()
        val adapter = FolderAdapter(folderList) { folderItem: FolderItem ->
            FolderVideosActivity.currentVideoList = folderItem.videoList
            FolderVideosActivity.folderName = folderItem.folderName
            startActivity(Intent(requireContext(), FolderVideosActivity::class.java))
        }
        recyclerView.adapter = adapter
    }
}
