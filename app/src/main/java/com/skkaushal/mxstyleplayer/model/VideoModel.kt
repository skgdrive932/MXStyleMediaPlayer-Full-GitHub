package com.skkaushal.mxstyleplayer.model

data class VideoModel(
    val id: Long,
    val title: String,
    val path: String,
    val duration: Long,
    val size: Long,
    val folderName: String
)
