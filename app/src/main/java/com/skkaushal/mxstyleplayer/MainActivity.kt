package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            initAppUI()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            initAppUI()
        }
    }

    private fun initAppUI() {
        try {
            val bottomNavId = resources.getIdentifier("bottomNavigation", "id", packageName)
            val bottomNav: BottomNavigationView? = if (bottomNavId != 0) findViewById(bottomNavId) else null

            // Default Fragment
            loadFragment(MusicFragment())

            bottomNav?.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    resources.getIdentifier("nav_music", "id", packageName) -> loadFragment(MusicFragment())
                    resources.getIdentifier("nav_video", "id", packageName) -> loadFragment(VideoFoldersFragment())
                    else -> false
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "UI Load error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        return try {
            val containerId = resources.getIdentifier("fragmentContainer", "id", packageName)
            val targetContainer = if (containerId != 0) containerId else android.R.id.content

            supportFragmentManager.beginTransaction()
                .replace(targetContainer, fragment)
                .commit()
            true
        } catch (e: Exception) {
            false
        }
    }
}
