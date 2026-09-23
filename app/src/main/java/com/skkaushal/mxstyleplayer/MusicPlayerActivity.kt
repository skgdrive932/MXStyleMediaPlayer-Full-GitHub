package com.skkaushal.mxstyleplayer

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.skkaushal.mxstyleplayer.model.VideoItem

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        var playlist: ArrayList<VideoItem> = ArrayList()
        var currentPosition: Int = 0
    }

    private lateinit var imgThumbnail: ImageView
    private lateinit var btnPlayPause: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var btnPrevious: ImageView
    private lateinit var txtSongTitle: TextView
    private lateinit var txtArtistName: TextView

    private lateinit var rotateAnimation: Animation
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        imgThumbnail = findViewById(R.id.imgMusicThumbnail)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)
        txtSongTitle = findViewById(R.id.txtSongTitle)
        txtArtistName = findViewById(R.id.txtArtistName)

        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        btnNext.setOnClickListener {
            if (playlist.isNotEmpty() && currentPosition < playlist.size - 1) {
                currentPosition++
                updateSongInfo()
            }
        }

        btnPrevious.setOnClickListener {
            if (playlist.isNotEmpty() && currentPosition > 0) {
                currentPosition--
                updateSongInfo()
            }
        }

        updateSongInfo()
        playMusic()
    }

    private fun updateSongInfo() {
        if (playlist.isNotEmpty() && currentPosition in playlist.indices) {
            val song = playlist[currentPosition]
            txtSongTitle.text = song.title
            txtArtistName.text = "Audio Track"
        }
    }

    private fun playMusic() {
        isPlaying = true
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
        imgThumbnail.startAnimation(rotateAnimation)
    }

    private fun pauseMusic() {
        isPlaying = false
        btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
        imgThumbnail.clearAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        imgThumbnail.clearAnimation()
    }
}
