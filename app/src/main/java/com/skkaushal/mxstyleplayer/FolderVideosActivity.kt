package com.skkaushal.mxstyleplayer

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

        val adapter = VideoAdapter(videoList)
        recyclerView.adapter = adapter
    }
}
