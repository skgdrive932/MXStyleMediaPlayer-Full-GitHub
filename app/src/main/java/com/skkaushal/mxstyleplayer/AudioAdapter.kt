package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.AudioItem

class AudioAdapter(
    private var audioList: List<AudioItem>,
    private val onItemClick: (AudioItem, Int) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(android.R.id.text1)
        val subtitleTextView: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(item: AudioItem, position: Int) {
            titleTextView.text = item.title
            subtitleTextView.text = if (item.songCount > 0) {
                "${item.artist} • ${item.songCount} Songs"
            } else {
                "${item.artist} • ${item.duration}"
            }

            itemView.setOnClickListener {
                onItemClick(item, position)
            }
        }
    }

    fun updateList(newList: List<AudioItem>) {
        this.audioList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audioList[position], position)
    }

    override fun getItemCount(): Int = audioList.size
}
