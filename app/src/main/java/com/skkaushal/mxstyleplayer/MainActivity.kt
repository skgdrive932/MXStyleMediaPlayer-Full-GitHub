package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.skkaushal.mxstyleplayer.databinding.ActivityMainBinding
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.model.VideoItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val folderList = ArrayList<FolderItem>()
    private lateinit var adapter: FolderAdapter
    private lateinit var repository: MediaStoreRepository

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = MediaStoreRepository(this)
        setupRecyclerView()
        checkAndRequestPermission()
    }

    private fun setupRecyclerView() {
        adapter = FolderAdapter(folderList) { folderItem ->
            if (folderItem.videoList.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(folderItem.videoList[0].uri, "video/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
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

        binding.txtVideoCount.text = "${folderList.size} folders"
    }
}
