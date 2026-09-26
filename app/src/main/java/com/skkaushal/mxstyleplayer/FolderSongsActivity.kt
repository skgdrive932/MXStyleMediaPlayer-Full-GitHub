package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.AudioItem
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class FolderSongsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvFolderName: TextView
    private lateinit var adapter: AudioAdapter
    private var folderSongsList: List<AudioItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_songs)

        val idFolderName = resources.getIdentifier("tvFolderName", "id", packageName)
        val idRvFolderSongs = resources.getIdentifier("rvFolderSongs", "id", packageName)

        tvFolderName = if (idFolderName != 0) findViewById(idFolderName) else findViewById(android.R.id.text1)
        recyclerView = if (idRvFolderSongs != 0) findViewById(idRvFolderSongs) else findViewById(resources.getIdentifier("recyclerViewMusic", "id", packageName))

        val targetFolder = intent.getStringExtra("FOLDER_NAME") ?: "Folder Songs"
        tvFolderName.text = targetFolder

        recyclerView.layoutManager = LinearLayoutManager(this)

        val repository = MediaStoreRepository(this)
        val allSongs = repository.getAllAudioTracks()

        // Filter based on album, artist, or song path matching folder name
        folderSongsList = allSongs.filter { song ->
            song.album.equals(targetFolder, ignoreCase = true) ||
            song.artist.equals(targetFolder, ignoreCase = true) ||
            song.uri.path?.contains(targetFolder, ignoreCase = true) == true
        }

        if (folderSongsList.isEmpty()) {
            folderSongsList = allSongs
        }

        adapter = AudioAdapter(folderSongsList) { _, position ->
            MusicPlayerActivity.musicPlaylist = folderSongsList
            MusicPlayerActivity.currentSongIndex = position

            val intent = Intent(this, MusicPlayerActivity::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}
