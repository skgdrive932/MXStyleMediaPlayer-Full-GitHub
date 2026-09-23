package com.skkaushal.mxstyleplayer

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.skkaushal.mxstyleplayer.model.VideoItem

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        var playlist: ArrayList<VideoItem> = ArrayList()
        var currentPosition: Int = 0
    }

    private lateinit var imgThumbnail: ImageView
    private lateinit var rotateAnimation: Animation
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        imgThumbnail = findViewById(R.id.imgMusicThumbnail)
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)

        imgThumbnail.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

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
