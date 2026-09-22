package com.skkaushal.mxstyleplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.skkaushal.mxstyleplayer.databinding.ActivityMainBinding
import com.skkaushal.mxstyleplayer.model.VideoItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val videoList = ArrayList<VideoItem>()
    private lateinit var adapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = VideoAdapter(videoList) { videoItem ->
            // Handle video click here
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}
