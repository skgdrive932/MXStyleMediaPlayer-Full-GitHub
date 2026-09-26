package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class FolderVideosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_videos)

        val folderName = intent.getStringExtra("FOLDER_NAME") ?: ""

        val rvId = resources.getIdentifier("recyclerViewFolderVideos", "id", packageName)
        val fallbackId = resources.getIdentifier("recyclerView", "id", packageName)
        
        recyclerView = when {
            rvId != 0 -> findViewById(rvId)
            fallbackId != 0 -> findViewById(fallbackId)
            else -> findViewById(android.R.id.list)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val repository = MediaStoreRepository(this)
        val folder = repository.getAllFolders().find { it.folderName == folderName }
        val videoList = folder?.videoList ?: emptyList()

        recyclerView.adapter = VideoAdapter(videoList) { selectedVideo, position ->
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_PATH", selectedVideo.path)
                putExtra("VIDEO_TITLE", selectedVideo.title)
            }
            startActivity(intent)
        }
    }
}
