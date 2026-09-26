package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.VideoItem

class VideoAdapter(
    private val videoList: List<VideoItem>,
    private val onItemClick: (VideoItem, Int) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(android.R.id.text1)
        val tvSubtitle: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.tvTitle.text = video.title
        holder.tvSubtitle.text = video.duration
        holder.itemView.setOnClickListener {
            onItemClick(video, position)
        }
    }

    override fun getItemCount(): Int = videoList.size
}
