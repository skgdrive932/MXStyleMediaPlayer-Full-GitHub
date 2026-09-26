package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.util.AudioRepository

class FolderSongsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: AudioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_songs)

        val folderPath = intent.getStringExtra("FOLDER_PATH") ?: ""

        val rvId = resources.getIdentifier("recyclerViewFolderSongs", "id", packageName)
        val fallbackId = resources.getIdentifier("recyclerView", "id", packageName)

        recyclerView = when {
            rvId != 0 -> findViewById(rvId)
            fallbackId != 0 -> findViewById(fallbackId)
            else -> findViewById(android.R.id.list)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        repository = AudioRepository(this)

        val folderSongs = repository.getAllAudioTracks().filter {
            it.path.startsWith(folderPath)
        }

        recyclerView.adapter = AudioAdapter(folderSongs) { selectedSong, position ->
            val intent = Intent(this, MusicPlayerActivity::class.java).apply {
                putExtra("SONG_PATH", selectedSong.path)
                putExtra("SONG_TITLE", selectedSong.title)
                putExtra("SONG_ARTIST", selectedSong.artist)
            }
            startActivity(intent)
        }
    }
}
