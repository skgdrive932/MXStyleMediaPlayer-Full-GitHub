package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.skkaushal.mxstyleplayer.model.AudioItem
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.util.AudioRepository
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private val folderList = ArrayList<FolderItem>()
    private val allAudioList = ArrayList<AudioItem>()
    private val displayedAudioList = ArrayList<AudioItem>()

    private lateinit var folderAdapter: FolderAdapter
    private lateinit var audioAdapter: AudioAdapter
    private lateinit var videoRepository: MediaStoreRepository
    private lateinit var audioRepository: AudioRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtHeader: TextView
    private lateinit var categoryTabs: TabLayout

    private var isVideoTab = true

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { loadData() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtHeader = findViewById(R.id.txtHeader)
        recyclerView = findViewById(R.id.recyclerViewMain)
        categoryTabs = findViewById(R.id.categoryTabs)
        val btnNavVideos = findViewById<Button>(R.id.btnNavVideos)
        val btnNavMusic = findViewById<Button>(R.id.btnNavMusic)

        videoRepository = MediaStoreRepository(this)
        audioRepository = AudioRepository(this)

        setupTabs()

        folderAdapter = FolderAdapter(folderList) { folderItem ->
            FolderVideosActivity.currentVideoList = folderItem.videoList
            FolderVideosActivity.folderName = folderItem.folderName
            startActivity(Intent(this, FolderVideosActivity::class.java))
        }

        audioAdapter = AudioAdapter(displayedAudioList) { audioItem ->
            val index = displayedAudioList.indexOf(audioItem)
            MusicPlayerActivity.playlist = displayedAudioList
            MusicPlayerActivity.currentPosition = if (index >= 0) index else 0
            startActivity(Intent(this, MusicPlayerActivity::class.java))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = folderAdapter

        btnNavVideos.setOnClickListener {
            isVideoTab = true
            txtHeader.text = "Video Folders"
            categoryTabs.visibility = View.GONE
            recyclerView.adapter = folderAdapter
        }

        btnNavMusic.setOnClickListener {
            isVideoTab = false
            txtHeader.text = "Music Library"
            categoryTabs.visibility = View.VISIBLE
            recyclerView.adapter = audioAdapter
            filterMusicByTab(categoryTabs.selectedTabPosition)
        }

        checkAndRequestPermissions()
    }

    private fun setupTabs() {
        categoryTabs.addTab(categoryTabs.newTab().setText("Tracks"))
        categoryTabs.addTab(categoryTabs.newTab().setText("Albums"))
        categoryTabs.addTab(categoryTabs.newTab().setText("Artists"))
        categoryTabs.addTab(categoryTabs.newTab().setText("Folders"))

        categoryTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { filterMusicByTab(it.position) }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun filterMusicByTab(position: Int) {
        displayedAudioList.clear()
        when (position) {
            0 -> displayedAudioList.addAll(allAudioList)
            1 -> displayedAudioList.addAll(allAudioList.distinctBy { it.album })
            2 -> displayedAudioList.addAll(allAudioList.distinctBy { it.artist })
            3 -> displayedAudioList.addAll(allAudioList.distinctBy { it.folderName })
        }
        audioAdapter.notifyDataSetChanged()
    }

    private fun checkAndRequestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        permissionLauncher.launch(permissions)
    }

    private fun loadData() {
        folderList.clear()
        folderList.addAll(videoRepository.getAllFolders())
        folderAdapter.notifyDataSetChanged()

        allAudioList.clear()
        allAudioList.addAll(audioRepository.getAllAudios())
        displayedAudioList.clear()
        displayedAudioList.addAll(allAudioList)
        audioAdapter.notifyDataSetChanged()
    }
}
