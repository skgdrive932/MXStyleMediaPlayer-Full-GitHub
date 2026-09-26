package com.skkaushal.mxstyleplayer.model

import android.net.Uri

data class AudioItem(
    val id: Long = 0L,
    val title: String = "",
    val artist: String = "Unknown Artist",
    val album: String = "Unknown Album",
    val path: String = "",
    val uri: Uri = Uri.EMPTY,
    val duration: String = "00:00",
    val songCount: Int = 0
)
