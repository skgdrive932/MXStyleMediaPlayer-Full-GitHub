package com.skkaushal.mxstyleplayer

import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.skkaushal.mxstyleplayer.model.AudioItem
import com.skkaushal.mxstyleplayer.service.MusicService

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        var playlist: List<AudioItem> = emptyList()
        var currentPosition: Int = 0
    }

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    private lateinit var txtSongTitle: TextView
    private lateinit var txtSongArtist: TextView
    private lateinit var txtCurrentTime: TextView
    private lateinit var txtTotalTime: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton

    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            mediaController?.let { controller ->
                if (controller.isPlaying) {
                    val currentMs = controller.currentPosition
                    val durationMs = controller.duration
                    if (durationMs > 0) {
                        seekBar.progress = ((currentMs * 100) / durationMs).toInt()
                        txtCurrentTime.text = formatTime(currentMs)
                        txtTotalTime.text = formatTime(durationMs)
                    }
                }
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        txtSongTitle = findViewById(R.id.txtSongTitle)
        txtSongArtist = findViewById(R.id.txtSongArtist)
        txtCurrentTime = findViewById(R.id.txtCurrentTime)
        txtTotalTime = findViewById(R.id.txtTotalTime)
        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, MusicService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture?.addListener({
            mediaController = controllerFuture?.get()
            initMediaController()
        }, MoreExecutors.directExecutor())
    }

    private fun initMediaController() {
        val controller = mediaController ?: return

        val mediaItems = playlist.map { MediaItem.fromUri(it.uri) }
        controller.setMediaItems(mediaItems, currentPosition, 0)
        controller.prepare()
        controller.play()

        updateUI(currentPosition)

        controller.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val index = controller.currentMediaItemIndex
                if (index in playlist.indices) {
                    currentPosition = index
                    updateUI(currentPosition)
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                btnPlayPause.setImageResource(
                    if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
                )
            }
        })

        handler.post(updateProgressRunnable)
    }

    private fun setupListeners() {
        btnPlayPause.setOnClickListener {
            mediaController?.let { controller ->
                if (controller.isPlaying) controller.pause() else controller.play()
            }
        }

        btnNext.setOnClickListener {
            mediaController?.seekToNextMediaItem()
        }

        btnPrevious.setOnClickListener {
            mediaController?.seekToPreviousMediaItem()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaController?.let { controller ->
                        val duration = controller.duration
                        if (duration > 0) {
                            val newPosition = (duration * progress) / 100
                            controller.seekTo(newPosition)
                        }
                    }
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun updateUI(index: Int) {
        if (index in playlist.indices) {
            val item = playlist[index]
            txtSongTitle.text = item.title
            txtSongArtist.text = item.artist
        }
    }

    private fun formatTime(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateProgressRunnable)
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }
}
