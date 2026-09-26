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

        tvFolderName = findViewById(resources.getIdentifier("tvFolderName", "id", packageName))
            ?: findViewById(android.R.id.text1)
        recyclerView = findViewById(resources.getIdentifier("rvFolderSongs", "id", packageName))
            ?: findViewById(resources.getIdentifier("recyclerViewMusic", "id", packageName))

        val folderName = intent.getStringExtra("FOLDER_NAME") ?: "Folder Songs"
        tvFolderName.text = folderName

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Storage se is particular folder ke songs fetch karna
        val repository = MediaStoreRepository(this)
        val allSongs = repository.getAllAudioTracks()
        folderSongsList = allSongs.filter { it.folderName == folderName || it.album == folderName || it.artist == folderName }

        // Agar filter se khali mile toh fallback saare songs
        if (folderSongsList.isEmpty()) {
            folderSongsList = allSongs
        }

        adapter = AudioAdapter(folderSongsList) { item, position ->
            MusicPlayerActivity.musicPlaylist = folderSongsList
            MusicPlayerActivity.currentSongIndex = position

            val intent = Intent(this, MusicPlayerActivity::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}
