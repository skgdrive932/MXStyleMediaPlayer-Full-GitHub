package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.AudioItem

class AudioAdapter(
    private val audioList: List<AudioItem>,
    private val onAudioClick: (AudioItem) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAudioTitle: TextView = itemView.findViewById(R.id.txtAudioTitle)
        val txtAudioArtist: TextView = itemView.findViewById(R.id.txtAudioArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_audio, parent, false
        )
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audio = audioList[position]
        holder.txtAudioTitle.text = audio.title
        holder.txtAudioArtist.text = audio.artist

        holder.itemView.setOnClickListener {
            onAudioClick(audio)
        }
    }

    override fun getItemCount(): Int = audioList.size
}
