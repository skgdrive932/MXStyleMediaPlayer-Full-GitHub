package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skkaushal.mxstyleplayer.databinding.ItemVideoBinding
import com.skkaushal.mxstyleplayer.model.VideoItem

class VideoAdapter(
    private val videoList: List<VideoItem>,
    private val onVideoClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]

        holder.binding.txtTitle.text = video.title
        holder.binding.txtDurationBadge.text = video.duration

        Glide.with(holder.itemView.context)
            .load(video.uri)
            .centerCrop()
            .placeholder(android.R.color.darker_gray)
            .into(holder.binding.imgThumbnail)

        holder.itemView.setOnClickListener {
            onVideoClick(video)
        }
    }

    override fun getItemCount(): Int = videoList.size
}
