package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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

        supportActionBar?.apply {
            title = folderName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFolderVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = VideoAdapter(currentVideoList) { videoItem ->
            val index = currentVideoList.indexOf(videoItem)
            PlayerActivity.videoList = currentVideoList
            PlayerActivity.currentPosition = if (index >= 0) index else 0
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
