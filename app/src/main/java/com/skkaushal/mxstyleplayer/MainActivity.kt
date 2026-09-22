package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.AudioItem
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.util.AudioRepository
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private val folderList = ArrayList<FolderItem>()
    private val audioList = ArrayList<AudioItem>()

    private lateinit var folderAdapter: FolderAdapter
    private lateinit var audioAdapter: AudioAdapter
    private lateinit var videoRepository: MediaStoreRepository
    private lateinit var audioRepository: AudioRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtHeader: TextView

    private var isVideoTab = true

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val videoGranted = permissions[Manifest.permission.READ_MEDIA_VIDEO] ?: false
        val audioGranted = permissions[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        val storageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (videoGranted || audioGranted || storageGranted) {
            loadData()
        } else {
            Toast.makeText(this, "Permissions required to load media", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtHeader = findViewById(R.id.txtHeader)
        recyclerView = findViewById(R.id.recyclerViewMain)
        val btnNavVideos = findViewById<Button>(R.id.btnNavVideos)
        val btnNavMusic = findViewById<Button>(R.id.btnNavMusic)

        videoRepository = MediaStoreRepository(this)
        audioRepository = AudioRepository(this)

        folderAdapter = FolderAdapter(folderList) { folderItem ->
            FolderVideosActivity.currentVideoList = folderItem.videoList
            FolderVideosActivity.folderName = folderItem.folderName
            startActivity(Intent(this, FolderVideosActivity::class.java))
        }

        audioAdapter = AudioAdapter(audioList) { audioItem ->
            val intent = Intent(this, MusicPlayerActivity::class.java).apply {
                putExtra("AUDIO_TITLE", audioItem.title)
                putExtra("AUDIO_ARTIST", audioItem.artist)
                putExtra("AUDIO_URI", audioItem.uri.toString())
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = folderAdapter

        btnNavVideos.setOnClickListener {
            if (!isVideoTab) {
                isVideoTab = true
                txtHeader.text = "Video Folders"
                recyclerView.adapter = folderAdapter
            }
        }

        btnNavMusic.setOnClickListener {
            if (isVideoTab) {
                isVideoTab = false
                txtHeader.text = "All Music Track"
                recyclerView.adapter = audioAdapter
            }
        }

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        permissionLauncher.launch(permissions)
    }

    private fun loadData() {
        // Load Video Folders
        folderList.clear()
        folderList.addAll(videoRepository.getAllFolders())
        folderAdapter.notifyDataSetChanged()

        // Load Audios
        audioList.clear()
        audioList.addAll(audioRepository.getAllAudios())
        audioAdapter.notifyDataSetChanged()
    }
}
