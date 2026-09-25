package com.skkaushal.mxstyleplayer

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private var playerView: VideoView? = null
    private lateinit var gestureDetector: GestureDetector
    private lateinit var audioManager: AudioManager

    private var screenWidth = 0
    private var maxVolume = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.videoView)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        screenWidth = resources.displayMetrics.widthPixels

        val videoUriStr = intent.getStringExtra("VIDEO_URI")
        if (!videoUriStr.isNullOrEmpty()) {
            playerView?.setVideoURI(Uri.parse(videoUriStr))
            playerView?.setOnPreparedListener {
                it.start()
            }
        }

        setupGestures()

        playerView?.setOnTouchListener { _, event ->
            if (::gestureDetector.isInitialized) {
                gestureDetector.onTouchEvent(event)
            }
            true
        }
    }

    private fun setupGestures() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (e1 == null) return false

                val deltaX = e2.x - e1.x
                val deltaY = e1.y - e2.y

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > 50) {
                        val seekAmount = (deltaX / 10).toInt() * 1000
                        val current = playerView?.currentPosition ?: 0
                        val duration = playerView?.duration ?: 0
                        val newPos = (current + seekAmount).coerceIn(0, duration)
                        playerView?.seekTo(newPos)
                    }
                } else {
                    if (e1.x < screenWidth / 2) {
                        adjustBrightness(deltaY)
                    } else {
                        adjustVolume(deltaY)
                    }
                }
                return true
            }
        })
    }

    private fun adjustVolume(deltaY: Float) {
        val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val change = if (deltaY > 0) 1 else -1
        val newVol = (currentVol + change).coerceIn(0, maxVolume)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, AudioManager.FLAG_SHOW_UI)
    }

    private fun adjustBrightness(deltaY: Float) {
        val layoutParams = window.attributes
        var currentBrightness = layoutParams.screenBrightness
        if (currentBrightness < 0) currentBrightness = 0.5f

        val change = if (deltaY > 0) 0.05f else -0.05f
        layoutParams.screenBrightness = (currentBrightness + change).coerceIn(0.01f, 1.0f)
        window.attributes = layoutParams
    }
}
