package com.skkaushal.mxstyleplayer.model

import android.net.Uri

data class AudioItem(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long = 0L,
    val uri: Uri,
    val dataPath: String = "",
    val songCount: Int = 0
)
