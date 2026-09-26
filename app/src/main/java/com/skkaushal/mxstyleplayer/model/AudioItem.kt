package com.skkaushal.mxstyleplayer.model

import android.net.Uri

data class AudioItem(
    val id: Long,
    val title: String,
    val artist: String = "Unknown Artist",
    val album: String = "Unknown Album",
    val path: String = "",
    val uri: Uri? = null,
    val duration: String = "00:00"
)
