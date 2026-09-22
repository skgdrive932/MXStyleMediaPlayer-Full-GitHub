package com.skkaushal.mxstyleplayer

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlin.math.abs

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var txtGestureIndicator: TextView
    private lateinit var gestureDetector: GestureDetector

    private val hideHandler = Handler(Looper.getMainLooper())
    private val hideRunnable = Runnable {
        txtGestureIndicator.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)
        txtGestureIndicator = findViewById(R.id.txtGestureIndicator)

        val videoUriString = intent.getStringExtra("VIDEO_URI")
        if (videoUriString != null) {
            setupPlayer(Uri.parse(videoUriString))
        }

        setupGestureDetector()

        playerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    private fun setupPlayer(videoUri: Uri) {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false

                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            seekVideo(10000)
                            showGestureText("+10s ⏩")
                        } else {
                            seekVideo(-10000)
                            showGestureText("-10s ⏪")
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun seekVideo(milliseconds: Long) {
        val currentPosition = player.currentPosition
        val newPosition = (currentPosition + milliseconds).coerceIn(0, player.duration)
        player.seekTo(newPosition)
    }

    private fun showGestureText(text: String) {
        txtGestureIndicator.text = text
        txtGestureIndicator.visibility = View.VISIBLE

        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, 1000)
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}
