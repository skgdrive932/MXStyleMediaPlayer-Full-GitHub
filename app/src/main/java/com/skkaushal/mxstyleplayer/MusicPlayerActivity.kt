package com.skkaushal.mxstyleplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var btnPlayPause: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        val txtSongTitle = findViewById<TextView>(R.id.txtSongTitle)
        val txtSongArtist = findViewById<TextView>(R.id.txtSongArtist)
        btnPlayPause = findViewById(R.id.btnPlayPause)

        val title = intent.getStringExtra("AUDIO_TITLE") ?: "Song"
        val artist = intent.getStringExtra("AUDIO_ARTIST") ?: "Artist"
        val uriString = intent.getStringExtra("AUDIO_URI")

        txtSongTitle.text = title
        txtSongArtist.text = artist

        if (uriString != null) {
            setupAudioPlayer(Uri.parse(uriString))
        }

        btnPlayPause.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                btnPlayPause.text = "Play"
            } else {
                player.play()
                btnPlayPause.text = "Pause"
            }
        }
    }

    private fun setupAudioPlayer(uri: Uri) {
        player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        if (::player.isInitialized) {
            player.release()
        }
    }
}
