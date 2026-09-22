package com.skkaushal.mxstyleplayer.model

data class FolderModel(
    val folderName: String,
    val videoList: ArrayList<VideoModel> = ArrayList()
)
