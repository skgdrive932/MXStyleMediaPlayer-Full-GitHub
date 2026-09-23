package com.skkaushal.mxstyleplayer.util

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.AudioItem

class AudioRepository(private val context: Context) {

    fun getAllAudios(): List<AudioItem> {
        val audioList = mutableListOf<AudioItem>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )

        cursor?.use { c ->
            val idCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (c.moveToNext()) {
                val id = c.getLong(idCol)
                val title = c.getString(titleCol) ?: "Unknown"
                val artist = c.getString(artistCol) ?: "Unknown Artist"
                val album = c.getString(albumCol) ?: "Unknown Album"
                val duration = c.getLong(durationCol)
                val path = c.getString(dataCol) ?: ""
                
                val folderName = path.substringBeforeLast("/").substringAfterLast("/")

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                )

                audioList.add(AudioItem(id, title, artist, album, folderName, duration, contentUri))
            }
        }
        return audioList
    }
}
