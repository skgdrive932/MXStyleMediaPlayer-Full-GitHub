package com.skkaushal.mxstyleplayer.util

import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.model.VideoItem

class MediaStoreRepository(private val context: Context) {

    fun getAllFolders(): List<FolderItem> {
        val folderMap = HashMap<String, MutableList<VideoItem>>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val folderColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn) ?: "Unknown Video"
                val path = it.getString(pathColumn) ?: ""
                val durationMs = it.getLong(durationColumn)
                val folderName = it.getString(folderColumn) ?: "Internal Storage"

                val seconds = (durationMs / 1000) % 60
                val minutes = (durationMs / (1000 * 60)) % 60
                val hours = durationMs / (1000 * 60 * 60)
                val durationStr = if (hours > 0) {
                    String.format("%02d:%02d:%02d", hours, minutes, seconds)
                } else {
                    String.format("%02d:%02d", minutes, seconds)
                }

                val video = VideoItem(id, title, path, durationStr)

                if (!folderMap.containsKey(folderName)) {
                    folderMap[folderName] = mutableListOf()
                }
                folderMap[folderName]?.add(video)
            }
        }

        return folderMap.map { (folderName, videoList) ->
            FolderItem(folderName, videoList)
        }
    }
}
