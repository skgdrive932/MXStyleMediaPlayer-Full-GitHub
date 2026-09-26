package com.skkaushal.mxstyleplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        setupAppUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            viewPager = findViewById(R.id.viewPager)
            tabLayout = findViewById(R.id.tabLayout)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        checkPermissionsAndInit()
    }

    private fun checkPermissionsAndInit() {
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
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            setupAppUI()
        }
    }

    private fun setupAppUI() {
        try {
            if (::viewPager.isInitialized && ::tabLayout.isInitialized) {
                val adapter = MainPagerAdapter(this)
                viewPager.adapter = adapter

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.text = "Videos"
                        1 -> tab.text = "Music"
                    }
                }.attach()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
