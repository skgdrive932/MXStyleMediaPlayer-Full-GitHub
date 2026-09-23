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
    private var tvTitle: TextView? = null
    private var tvArtist: TextView? = null
    private var btnPlayPause: ImageView? = null
    private var seekBar: SeekBar? = null
    private var tvCurrentTime: TextView? = null
    private var tvTotalTime: TextView? = null

    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        // Null-safe ID mapping
        tvTitle = findViewById(R.id.tvSongTitle) ?: findViewById(R.id.txtSongTitle)
        tvArtist = findViewById(R.id.tvArtistName) ?: findViewById(R.id.txtArtistName)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        seekBar = findViewById(R.id.seekBar)
        tvCurrentTime = findViewById(R.id.tvCurrentTime) ?: findViewById(R.id.txtCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime) ?: findViewById(R.id.txtTotalTime)

        val title = intent.getStringExtra("SONG_TITLE") ?: "Song Title"
        val artist = intent.getStringExtra("SONG_ARTIST") ?: "Artist Name"
        val songUriStr = intent.getStringExtra("SONG_URI")

        tvTitle?.text = title
        tvArtist?.text = artist

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
            tvTotalTime?.text = formatTime(mediaPlayer?.duration ?: 0)
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
                        tvCurrentTime?.text = formatTime(it.currentPosition)
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
