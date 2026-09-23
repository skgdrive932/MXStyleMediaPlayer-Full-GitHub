package com.skkaushal.mxstyleplayer

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.skkaushal.mxstyleplayer.model.AudioItem
import java.util.Locale

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        var playlist: List<AudioItem> = ArrayList()
        var currentPosition: Int = 0
    }

    private var player: ExoPlayer? = null
    private lateinit var txtTitle: TextView
    private lateinit var txtArtist: TextView
    private lateinit var txtCurrentTime: TextView
    private lateinit var txtTotalTime: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var btnPrevious: ImageView

    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            player?.let {
                if (it.isPlaying) {
                    seekBar.progress = it.currentPosition.toInt()
                    txtCurrentTime.text = formatTime(it.currentPosition)
                    handler.postDelayed(this, 1000)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        txtTitle = findViewById(R.id.txtSongTitle)
        txtArtist = findViewById(R.id.txtArtistName)
        txtCurrentTime = findViewById(R.id.txtCurrentTime)
        txtTotalTime = findViewById(R.id.txtTotalTime)
        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)

        setupPlayer()

        btnPlayPause.setOnClickListener {
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
                } else {
                    it.play()
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
                    handler.post(updateProgressRunnable)
                }
            }
        }

        btnNext.setOnClickListener {
            if (playlist.isNotEmpty() && currentPosition < playlist.size - 1) {
                currentPosition++
                playCurrentAudio()
            }
        }

        btnPrevious.setOnClickListener {
            if (playlist.isNotEmpty() && currentPosition > 0) {
                currentPosition--
                playCurrentAudio()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.seekTo(progress.toLong())
                    txtCurrentTime.text = formatTime(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    val duration = player?.duration ?: 0L
                    seekBar.max = duration.toInt()
                    txtTotalTime.text = formatTime(duration)
                    handler.post(updateProgressRunnable)
                }
            }
        })
        playCurrentAudio()
    }

    private fun playCurrentAudio() {
        if (playlist.isEmpty() || currentPosition !in playlist.indices) return

        val audio = playlist[currentPosition]
        txtTitle.text = audio.title
        txtArtist.text = audio.artist

        player?.stop()
        val mediaItem = MediaItem.fromUri(audio.uri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
    }

    private fun formatTime(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateProgressRunnable)
        player?.release()
        player = null
    }
}
