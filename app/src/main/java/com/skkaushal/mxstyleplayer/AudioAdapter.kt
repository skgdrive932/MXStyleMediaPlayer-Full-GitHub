package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.AudioItem

class AudioAdapter(
    private val audioList: List<AudioItem>,
    private val onItemClick: (AudioItem) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtFolderName)
        val txtSubTitle: TextView = itemView.findViewById(R.id.txtVideoCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audio = audioList[position]
        holder.txtTitle.text = audio.title
        holder.txtSubTitle.text = "${audio.artist} • ${audio.album}"

        holder.itemView.setOnClickListener {
            onItemClick(audio)
        }
    }

    override fun getItemCount(): Int = audioList.size
}
