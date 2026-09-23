package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private lateinit var repository: MediaStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = MediaStoreRepository(this)
        val folderList = repository.getAllFolders()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFolders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = FolderAdapter(folderList) { folderItem ->
            FolderVideosActivity.currentVideoList = folderItem.videos
            FolderVideosActivity.folderName = folderItem.name
            startActivity(Intent(this, FolderVideosActivity::class.java))
        }
        recyclerView.adapter = adapter
    }
}
