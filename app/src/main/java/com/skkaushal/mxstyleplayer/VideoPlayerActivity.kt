package com.skkaushal.mxstyleplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideoPlayerActivity : AppCompatActivity() {

    companion object {
        var videoUri: Uri? = null
        var videoTitle: String? = null
    }

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)

        val uriExtra = intent.getStringExtra("VIDEO_URI")
        val uriToPlay = videoUri ?: if (!uriExtra.isNullOrEmpty()) Uri.parse(uriExtra) else null

        if (uriToPlay == null) {
            Toast.makeText(this, "Cannot play video: Invalid URI", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initializePlayer(uriToPlay)
    }

    private fun initializePlayer(uri: Uri) {
        try {
            player = ExoPlayer.Builder(this).build()
            playerView.player = player

            val mediaItem = MediaItem.fromUri(uri)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.playWhenReady = true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Playback Error: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
