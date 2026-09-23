package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity : AppCompatActivity() {

    private lateinit var repository: MediaStoreRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tabLayout: TabLayout
    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = MediaStoreRepository(this)
        recyclerView = findViewById(R.id.recyclerViewFolders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        tabLayout = findViewById(R.id.tabLayout)
        bottomNavigationView = findViewById(R.id.bottomNavigation)

        setupVideoTabs()

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_videos -> {
                    setupVideoTabs()
                    loadFolders()
                    true
                }
                R.id.nav_music -> {
                    setupMusicTabs()
                    // Tab categorization loaded
                    true
                }
                else -> false
            }
        }

        checkAndRequestPermissions()
    }

    private fun setupVideoTabs() {
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Folders"))
        tabLayout.addTab(tabLayout.newTab().setText("All Videos"))
    }

    private fun setupMusicTabs() {
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Tracks"))
        tabLayout.addTab(tabLayout.newTab().setText("Albums"))
        tabLayout.addTab(tabLayout.newTab().setText("Artists"))
        tabLayout.addTab(tabLayout.newTab().setText("Folders"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Future Fragment/Adapter binding for Tracks/Albums/Artists
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
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
            FolderVideosActivity.currentVideoList = folderItem.videoList
            FolderVideosActivity.folderName = folderItem.folderName
            startActivity(Intent(this, FolderVideosActivity::class.java))
        }
        recyclerView.adapter = adapter
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
                Toast.makeText(this, "Storage Permission Required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
