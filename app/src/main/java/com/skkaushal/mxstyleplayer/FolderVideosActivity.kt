package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.VideoItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class FolderVideosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvFolderName: TextView
    private lateinit var adapter: VideoAdapter
    private var videoList: List<VideoItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_videos)

        val idFolderName = resources.getIdentifier("tvFolderName", "id", packageName)
        val idRvFolderVideos = resources.getIdentifier("rvFolderVideos", "id", packageName)

        tvFolderName = if (idFolderName != 0) findViewById(idFolderName) else findViewById(android.R.id.text1)
        recyclerView = if (idRvFolderVideos != 0) findViewById(idRvFolderVideos) else findViewById(resources.getIdentifier("recyclerViewVideo", "id", packageName))

        val targetFolder = intent.getStringExtra("FOLDER_NAME") ?: "Videos"
        tvFolderName.text = targetFolder

        recyclerView.layoutManager = LinearLayoutManager(this)

        val repository = MediaStoreRepository(this)

        val allVideos = try {
            repository.getVideos()
        } catch (e: Exception) {
            emptyList()
        }

        videoList = allVideos.filter { video ->
            video.title.contains(targetFolder, ignoreCase = true) ||
            video.path.contains(targetFolder, ignoreCase = true)
        }

        if (videoList.isEmpty()) {
            videoList = allVideos
        }

        adapter = VideoAdapter(videoList) { videoItem, _ ->
            try {
                val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                    putExtra("VIDEO_URI", videoItem.path)
                    putExtra("VIDEO_TITLE", videoItem.title)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Video play nahi ho pa rahi hai", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter = adapter
    }
}
