package com.skkaushal.mxstyleplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MusicPlayerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var txtTitle: TextView? = null
    private var txtArtist: TextView? = null
    private var btnPlayPause: ImageView? = null
    private var seekBar: SeekBar? = null
    private var txtCurrentTime: TextView? = null
    private var txtTotalTime: TextView? = null

    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        txtTitle = findViewById(resources.getIdentifier("tvSongTitle", "id", packageName)) 
            ?: findViewById(resources.getIdentifier("txtSongTitle", "id", packageName))
        txtArtist = findViewById(resources.getIdentifier("tvArtistName", "id", packageName)) 
            ?: findViewById(resources.getIdentifier("txtArtistName", "id", packageName))
        btnPlayPause = findViewById(resources.getIdentifier("btnPlayPause", "id", packageName))
        seekBar = findViewById(resources.getIdentifier("seekBar", "id", packageName))
        txtCurrentTime = findViewById(resources.getIdentifier("tvCurrentTime", "id", packageName)) 
            ?: findViewById(resources.getIdentifier("txtCurrentTime", "id", packageName))
        txtTotalTime = findViewById(resources.getIdentifier("tvTotalTime", "id", packageName)) 
            ?: findViewById(resources.getIdentifier("txtTotalTime", "id", packageName))

        val title = intent.getStringExtra("SONG_TITLE") ?: "Song Title"
        val artist = intent.getStringExtra("SONG_ARTIST") ?: "Artist Name"
        val songUriStr = intent.getStringExtra("SONG_URI")

        txtTitle?.text = title
        txtArtist?.text = artist

        if (!songUriStr.isNullOrEmpty()) {
            initMediaPlayer(Uri.parse(songUriStr))
        }

        btnPlayPause?.setOnClickListener {
            if (isPlaying) pauseSong() else playSong()
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun initMediaPlayer(uri: Uri) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, uri)
                prepare()
                start()
            }
            isPlaying = true
            btnPlayPause?.setImageResource(android.R.drawable.ic_media_pause)
            seekBar?.max = mediaPlayer?.duration ?: 0
            txtTotalTime?.text = formatTime(mediaPlayer?.duration ?: 0)
            updateSeekBar()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playSong() {
        mediaPlayer?.start()
        isPlaying = true
        btnPlayPause?.setImageResource(android.R.drawable.ic_media_pause)
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
        isPlaying = false
        btnPlayPause?.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        seekBar?.progress = it.currentPosition
                        txtCurrentTime?.text = formatTime(it.currentPosition)
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    private fun formatTime(ms: Int): String {
        val min = (ms / 1000) / 60
        val sec = (ms / 1000) % 60
        return String.format("%02d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        handler.removeCallbacksAndMessages(null)
    }
}
