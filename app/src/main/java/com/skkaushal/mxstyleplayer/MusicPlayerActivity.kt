package com.skkaushal.mxstyleplayer

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var imgThumbnail: ImageView
    private lateinit var rotateAnimation: Animation
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        imgThumbnail = findViewById(R.id.imgMusicThumbnail)

        // Animation Load Karein
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)

        // Sample Play/Pause Click Control
        imgThumbnail.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        // Auto start on open
        playMusic()
    }

    private fun playMusic() {
        isPlaying = true
        imgThumbnail.startAnimation(rotateAnimation)
    }

    private fun pauseMusic() {
        isPlaying = false
        imgThumbnail.clearAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        imgThumbnail.clearAnimation()
    }
}
