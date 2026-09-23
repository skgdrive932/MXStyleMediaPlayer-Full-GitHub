package com.skkaushal.mxstyleplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skkaushal.mxstyleplayer.model.VideoItem

class PlayerActivity : AppCompatActivity() {

    companion object {
        var videoList: ArrayList<VideoItem> = ArrayList()
        var currentPosition: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }
}
