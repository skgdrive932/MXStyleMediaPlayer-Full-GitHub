package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.VideoItem

class FolderVideosActivity : AppCompatActivity() {

    companion object {
        var videoList: List<VideoItem> = emptyList()
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_videos)

        recyclerView = findViewById(R.id.recyclerViewFolderVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = VideoAdapter(videoList) { videoItem ->
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_URI", videoItem.uri.toString())
                putExtra("VIDEO_TITLE", videoItem.title)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
