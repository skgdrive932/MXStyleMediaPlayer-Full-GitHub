package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.VideoItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class VideoFoldersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: MediaStoreRepository
    private lateinit var adapter: VideoAdapter

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

        val videos = try {
            repository.getVideos()
        } catch (e: Exception) {
            emptyList<VideoItem>()
        }

        adapter = VideoAdapter(videos) { videoItem, _ ->
            val intent = Intent(requireContext(), VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_URI", videoItem.path)
                putExtra("VIDEO_TITLE", videoItem.title)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        return view
    }
}
