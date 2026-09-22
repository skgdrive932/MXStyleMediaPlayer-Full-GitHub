package com.skkaushal.mxstyleplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
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
    private lateinit var audioManager: AudioManager

    private var maxVolume = 0
    private var currentVolume = 0
    private var currentBrightness = 0.5f

    private val hideHandler = Handler(Looper.getMainLooper())
    private val hideRunnable = Runnable {
        txtGestureIndicator.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)
        txtGestureIndicator = findViewById(R.id.txtGestureIndicator)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val videoUriString = intent.getStringExtra("VIDEO_URI")
        if (videoUriString != null) {
            setupPlayer(Uri.parse(videoUriString))
        }

        setupGestureDetector()

        playerView.setOnTouchListener { _, event ->
            if (gestureDetector.onTouchEvent(event)) {
                true
            } else {
                if (event.action == MotionEvent.ACTION_UP) {
                    hideHandler.postDelayed(hideRunnable, 1000)
                }
                false
            }
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
            private val SWIPE_THRESHOLD = 50

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (e1 == null) return false

                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                val screenWidth = resources.displayMetrics.widthPixels

                if (abs(diffY) > abs(diffX)) { // Vertical Scroll
                    if (abs(diffY) > SWIPE_THRESHOLD) {
                        if (e1.x < screenWidth / 2) {
                            // Left Side Vertical Scroll -> Brightness Control
                            changeBrightness(distanceY)
                        } else {
                            // Right Side Vertical Scroll -> Volume Control
                            changeVolume(distanceY)
                        }
                        return true
                    }
                }
                return false
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false

                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                if (abs(diffX) > abs(diffY)) { // Horizontal Swipe -> Seek Video
                    if (abs(diffX) > 100) {
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

    private fun changeBrightness(distanceY: Float) {
        val layoutParams = window.attributes
        var brightness = layoutParams.screenBrightness
        if (brightness < 0) brightness = 0.5f // Default Screen Brightness

        // Swipe Up -> Increase, Swipe Down -> Decrease
        brightness += (distanceY / 1000f)
        brightness = brightness.coerceIn(0.01f, 1.0f)

        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams

        val percentage = (brightness * 100).toInt()
        showGestureText("☀️ Brightness: $percentage%")
    }

    private fun changeVolume(distanceY: Float) {
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val delta = if (distanceY > 0) 1 else -1

        val newVolume = (currentVolume + delta).coerceIn(0, maxVolume)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)

        val percentage = ((newVolume.toFloat() / maxVolume) * 100).toInt()
        showGestureText("🔊 Volume: $percentage%")
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
