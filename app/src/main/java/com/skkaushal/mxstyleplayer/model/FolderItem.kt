package com.skkaushal.mxstyleplayer.model

data class FolderItem(
    val folderName: String,
    val videoList: ArrayList<VideoItem> = ArrayList()
)
