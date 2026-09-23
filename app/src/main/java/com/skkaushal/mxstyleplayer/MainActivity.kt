package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private lateinit var repository: MediaStoreRepository
    private var recyclerView: RecyclerView? = null
    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = MediaStoreRepository(this)

        // Find RecyclerView safely matching activity_main.xml IDs
        recyclerView = findViewById(R.id.recyclerViewFolders) ?: findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), STORAGE_PERMISSION_CODE)
        } else {
            loadFolders()
        }
    }

    private fun loadFolders() {
        val folderList = repository.getAllFolders()

        val adapter = FolderAdapter(folderList) { folderItem: FolderItem ->
            FolderVideosActivity.currentVideoList = folderItem.videos
            FolderVideosActivity.folderName = folderItem.name
            startActivity(Intent(this, FolderVideosActivity::class.java))
        }
        recyclerView?.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadFolders()
            } else {
                Toast.makeText(this, "Storage Permission Required to Display Media", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
