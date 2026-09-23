package com.skkaushal.mxstyleplayer.util

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.AudioItem
import com.skkaushal.mxstyleplayer.model.FolderItem
import com.skkaushal.mxstyleplayer.model.VideoItem
import java.io.File
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

    fun getAllAudioTracks(): List<AudioItem> {
        val audioList = ArrayList<AudioItem>()
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
            MediaStore.Audio.Media.IS_MUSIC + "!= 0",
            null,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val title = it.getString(titleCol) ?: "Unknown Song"
                val artist = it.getString(artistCol) ?: "<Unknown Artist>"
                val album = it.getString(albumCol) ?: "Unknown Album"
                val duration = it.getLong(durationCol)
                val path = it.getString(dataCol) ?: ""
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                audioList.add(
                    AudioItem(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        uri = uri,
                        dataPath = path
                    )
                )
            }
        }
        return audioList
    }

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
                    duration = 0L,
                    uri = firstTrack.uri,
                    dataPath = firstTrack.dataPath,
                    songCount = tracks.size
                )
            }
            .sortedBy { it.title.lowercase() }
    }

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
                    duration = 0L,
                    uri = firstTrack.uri,
                    dataPath = firstTrack.dataPath,
                    songCount = tracks.size
                )
            }
            .sortedBy { it.title.lowercase() }
    }

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
                    duration = 0L,
                    uri = firstTrack.uri,
                    dataPath = firstTrack.dataPath,
                    songCount = tracks.size
                )
            }
            .sortedBy { it.title.lowercase() }
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
