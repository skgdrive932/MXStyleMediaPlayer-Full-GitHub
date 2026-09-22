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
import com.skkaushal.mxstyleplayer.model.VideoItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val videoList = ArrayList<VideoItem>()
    private lateinit var adapter: VideoAdapter
    private lateinit var repository: MediaStoreRepository

    // Permission launcher to handle storage permission request
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadVideos()
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

        // Floating Play Button Click Listener
        binding.fabPlay.setOnClickListener {
            if (videoList.isNotEmpty()) {
                playVideo(videoList[0])
            } else {
                Toast.makeText(this, "No videos available to play", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = VideoAdapter(videoList) { videoItem ->
            playVideo(videoItem)
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
            loadVideos()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    private fun loadVideos() {
        val fetchedVideos = repository.getAllVideos()
        videoList.clear()
        videoList.addAll(fetchedVideos)
        adapter.notifyDataSetChanged()

        binding.txtVideoCount.text = "${videoList.size} videos"
    }

    private fun playVideo(video: VideoItem) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(video.uri, "video/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }
}
