package com.skkaushal.mxstyleplayer.model

data class VideoItem(
    val name: String,
    val uri: String,
    val duration: Long,
    val size: Long,
    val folderName: String = "Internal Storage"
)
