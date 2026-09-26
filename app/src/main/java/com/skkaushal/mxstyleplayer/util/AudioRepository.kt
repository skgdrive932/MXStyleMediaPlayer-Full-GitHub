package com.skkaushal.mxstyleplayer.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
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
                    val artist = if (artistCol != -1) it.getString(artistCol) ?: "Unknown Artist" else "Unknown Artist"
                    val album = if (albumCol != -1) it.getString(albumCol) ?: "Unknown Album" else "Unknown Album"
                    val path = if (pathCol != -1) it.getString(pathCol) ?: "" else ""
                    val durationMs = if (durationCol != -1) it.getLong(durationCol) else 0L

                    val seconds = (durationMs / 1000) % 60
                    val minutes = (durationMs / (1000 * 60)) % 60
                    val durationStr = String.format("%02d:%02d", minutes, seconds)

                    val contentUri: Uri = if (id != 0L) {
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    } else {
                        Uri.EMPTY
                    }

                    audioList.add(
                        AudioItem(
                            id = id,
                            title = title,
                            artist = artist,
                            album = album,
                            path = path,
                            uri = contentUri,
                            duration = durationStr
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return audioList
    }

    fun getAlbums(): List<AudioItem> {
        val tracks = getAllAudioTracks()
        return tracks.groupBy { it.album }.map { (albumName, list) ->
            val first = list.first()
            first.copy(
                title = albumName,
                songCount = list.size
            )
        }
    }

    fun getArtists(): List<AudioItem> {
        val tracks = getAllAudioTracks()
        return tracks.groupBy { it.artist }.map { (artistName, list) ->
            val first = list.first()
            first.copy(
                title = artistName,
                songCount = list.size
            )
        }
    }

    fun getFolders(): List<AudioItem> {
        val tracks = getAllAudioTracks()
        return tracks.groupBy { it.path.substringBeforeLast('/', "Internal") }.map { (folderPath, list) ->
            val folderName = folderPath.substringAfterLast('/')
            val first = list.first()
            first.copy(
                title = folderName,
                path = folderPath,
                songCount = list.size
            )
        }
    }
}
