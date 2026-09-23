package com.skkaushal.mxstyleplayer.model

data class AudioItem(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val dataPath: String = "",
    val songCount: Int = 0
)
