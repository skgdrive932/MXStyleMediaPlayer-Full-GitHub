package com.skkaushal.mxstyleplayer.util

import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.AudioItem
import java.io.File

class MediaStoreRepository(private val context: Context) {

    // 1. Tracks Tab: Sabhi Audio Files
    fun getAllAudioTracks(): List<AudioItem> {
        val list = mutableListOf<AudioItem>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val title = it.getString(titleCol) ?: "Unknown Title"
                val artist = it.getString(artistCol) ?: "Unknown Artist"
                val album = it.getString(albumCol) ?: "Unknown Album"
                val path = it.getString(dataCol) ?: ""

                list.add(
                    AudioItem(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        dataPath = path
                    )
                )
            }
        }
        return list
    }

    // 2. Albums Tab: Album wise Grouped
    fun getAlbums(): List<AudioItem> {
        val allTracks = getAllAudioTracks()
        return allTracks
            .groupBy { if (it.album.isBlank() || it.album == "<unknown>") "Unknown Album" else it.album }
            .map { (albumName, tracks) ->
                val firstTrack = tracks.first()
                AudioItem(
                    id = firstTrack.id,
                    title = albumName,
                    artist = "${tracks.size} Songs",
                    album = albumName,
                    dataPath = firstTrack.dataPath,
                    songCount = tracks.size
                )
            }
            .sortedBy { it.title.lowercase() }
    }

    // 3. Artists Tab: Artist wise Grouped
    fun getArtists(): List<AudioItem> {
        val allTracks = getAllAudioTracks()
        return allTracks
            .groupBy { if (it.artist.isBlank() || it.artist == "<unknown>") "Unknown Artist" else it.artist }
            .map { (artistName, tracks) ->
                val firstTrack = tracks.first()
                AudioItem(
                    id = firstTrack.id,
                    title = artistName,
                    artist = "${tracks.size} Songs",
                    album = "Artist",
                    dataPath = firstTrack.dataPath,
                    songCount = tracks.size
                )
            }
            .sortedBy { it.title.lowercase() }
    }

    // 4. Folders Tab: Folder wise Grouped
    fun getFolders(): List<AudioItem> {
        val allTracks = getAllAudioTracks()
        return allTracks
            .groupBy { track ->
                if (track.dataPath.isNotBlank()) {
                    File(track.dataPath).parentFile?.name ?: "Internal Storage"
                } else {
                    "Unknown Folder"
                }
            }
            .map { (folderName, tracks) ->
                val firstTrack = tracks.first()
                AudioItem(
                    id = firstTrack.id,
                    title = folderName,
                    artist = "${tracks.size} Songs",
                    album = "Folder",
                    dataPath = firstTrack.dataPath,
                    songCount = tracks.size
                )
            }
            .sortedBy { it.title.lowercase() }
    }
}
