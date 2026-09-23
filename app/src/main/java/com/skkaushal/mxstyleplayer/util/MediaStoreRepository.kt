package com.skkaushal.mxstyleplayer.util

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.model.VideoItem
import java.util.Locale

class MediaStoreRepository(private val context: Context) {

    fun getAllFolders(): List<FolderItem> {
        val folderMap = HashMap<String, ArrayList<VideoItem>>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val bucketColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val rawName = it.getString(nameColumn) ?: "Unknown Video"
                val cleanName = if (rawName.contains(".")) rawName.substringBeforeLast(".") else rawName
                val durationMs = it.getLong(durationColumn)
                val formattedDuration = formatDuration(durationMs)
                
                val folderName = it.getString(bucketColumn) ?: "Internal Storage"
                val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                val videoItem = VideoItem(id, cleanName, formattedDuration, contentUri)

                if (!folderMap.containsKey(folderName)) {
                    folderMap[folderName] = ArrayList()
                }
                folderMap[folderName]?.add(videoItem)
            }
        }

        val folderList = ArrayList<FolderItem>()
        folderMap.forEach { (name, videos) ->
            folderList.add(FolderItem(name, videos))
        }
        return folderList
    }

    private fun formatDuration(durationMs: Long): String {
        val seconds = (durationMs / 1000) % 60
        val minutes = (durationMs / (1000 * 60)) % 60
        val hours = durationMs / (1000 * 60 * 60)

        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }
}
