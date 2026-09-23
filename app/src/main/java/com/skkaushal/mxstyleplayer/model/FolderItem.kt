package com.skkaushal.mxstyleplayer.model

import java.io.Serializable

data class FolderItem(
    val folderName: String,
    val videoList: ArrayList<VideoItem>
) : Serializable
