package com.skkaushal.mxstyleplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.skkaushal.mxstyleplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUriString = intent.getStringExtra("VIDEO_URI")
        if (videoUriString != null) {
            initializePlayer(Uri.parse(videoUriString))
        }
    }

    private fun initializePlayer(uri: Uri) {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            exoPlayer.release()
        }
        player = null
    }
}
