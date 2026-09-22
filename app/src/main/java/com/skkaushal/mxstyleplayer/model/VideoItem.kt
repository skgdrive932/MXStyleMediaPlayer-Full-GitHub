package com.skkaushal.mxstyleplayer.model

import android.net.Uri

data class VideoItem(
    val id: Long,
    val title: String,
    val duration: String,
    val uri: Uri
)
