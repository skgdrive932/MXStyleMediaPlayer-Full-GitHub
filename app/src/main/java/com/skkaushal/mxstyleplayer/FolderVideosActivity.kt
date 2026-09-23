package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.VideoItem

class FolderVideosActivity : AppCompatActivity() {

    companion object {
        var currentVideoList: List<VideoItem> = ArrayList()
        var folderName: String = "Videos"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_videos)

        val toolbar = findViewById<Toolbar>(R.id.toolbarFolderVideos)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = folderName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFolderVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = VideoAdapter(currentVideoList) { videoItem ->
            val index = currentVideoList.indexOf(videoItem)
            PlayerActivity.videoList = ArrayList(currentVideoList)
            PlayerActivity.currentPosition = if (index >= 0) index else 0
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        recyclerView.adapter = adapter
    }
}
