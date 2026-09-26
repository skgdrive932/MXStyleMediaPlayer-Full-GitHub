package com.skkaushal.mxstyleplayer.util

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.AudioItem

class AudioRepository(private val context: Context) {

    fun getAllAudioTracks(): List<AudioItem> {
        val audioList = mutableListOf<AudioItem>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        try {
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                val idCol = it.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleCol = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistCol = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val albumCol = it.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                val pathCol = it.getColumnIndex(MediaStore.Audio.Media.DATA)
                val durationCol = it.getColumnIndex(MediaStore.Audio.Media.DURATION)

                while (it.moveToNext()) {
                    val id = if (idCol != -1) it.getLong(idCol) else 0L
                    val title = if (titleCol != -1) it.getString(titleCol) ?: "Unknown Song" else "Unknown Song"
                    val artist = if (artistCol != -1) it.getString(artistCol) ?: "<unknown>" else "<unknown>"
                    val album = if (albumCol != -1) it.getString(albumCol) ?: "Unknown Album" else "Unknown Album"
                    val path = if (pathCol != -1) it.getString(pathCol) ?: "" else ""
                    val durationMs = if (durationCol != -1) it.getLong(durationCol) else 0L

                    val seconds = (durationMs / 1000) % 60
                    val minutes = (durationMs / (1000 * 60)) % 60
                    val durationStr = String.format("%02d:%02d", minutes, seconds)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    audioList.add(AudioItem(id, title, artist, album, path, contentUri, durationStr))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return audioList
    }

    fun getAlbums(): List<AudioItem> = getAllAudioTracks().distinctBy { it.album }

    fun getArtists(): List<AudioItem> = getAllAudioTracks().distinctBy { it.artist }

    fun getFolders(): List<AudioItem> = getAllAudioTracks().distinctBy { 
        it.path.substringBeforeLast('/') 
    }
}
