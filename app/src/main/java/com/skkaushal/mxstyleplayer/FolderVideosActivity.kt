package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.VideoItem

class FolderVideosActivity : AppCompatActivity() {

    companion object {
        var currentVideoList: List<VideoItem> = emptyList()
        var folderName: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_videos)

        val txtFolderTitle = findViewById<TextView>(R.id.txtFolderTitle)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFolderVideos)

        txtFolderTitle.text = folderName

        val adapter = VideoAdapter(currentVideoList) { videoItem ->
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_URI", videoItem.uri.toString())
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
