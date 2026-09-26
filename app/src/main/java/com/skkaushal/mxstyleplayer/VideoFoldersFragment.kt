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

        val idRv = resources.getIdentifier("recyclerViewVideoFolders", "id", requireContext().packageName)
        recyclerView = if (idRv != 0) view.findViewById(idRv) else view.findViewById(resources.getIdentifier("recyclerViewVideo", "id", requireContext().packageName))

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        repository = MediaStoreRepository(requireContext())

        val folderList: List<FolderItem> = repository.getAllFolders()

        recyclerView.adapter = VideoFolderAdapter(folderList) { selectedFolder ->
            val intent = Intent(requireContext(), FolderVideosActivity::class.java).apply {
                putExtra("FOLDER_NAME", selectedFolder.folderName)
            }
            startActivity(intent)
        }

        return view
    }
}
