package com.skkaushal.mxstyleplayer.model

import java.io.Serializable

data class FolderItem(
    val name: String,
    val videos: ArrayList<VideoItem>
) : Serializable
