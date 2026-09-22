package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private val folderList = ArrayList<FolderItem>()
    private lateinit var adapter: FolderAdapter
    private lateinit var repository: MediaStoreRepository
    private lateinit var txtVideoCount: TextView

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadFolders()
        } else {
            Toast.makeText(this, "Permission denied to read videos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtVideoCount = findViewById(R.id.txtVideoCount)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        repository = MediaStoreRepository(this)

        adapter = FolderAdapter(folderList) { folderItem ->
            // Folder click hone par FolderVideosActivity open hogi aur video list pass hogi
            FolderVideosActivity.currentVideoList = folderItem.videoList
            FolderVideosActivity.folderName = folderItem.folderName
            val intent = Intent(this, FolderVideosActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadFolders()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    private fun loadFolders() {
        val fetchedFolders = repository.getAllFolders()
        folderList.clear()
        folderList.addAll(fetchedFolders)
        adapter.notifyDataSetChanged()

        txtVideoCount.text = "${folderList.size} folders"
    }
}
