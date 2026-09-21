package com.skkaushal.mxstyleplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.databinding.ItemVideoBinding
import com.skkaushal.mxstyleplayer.model.VideoItem

class VideoAdapter(
    private val context: Context,
    private val videoList: List<VideoItem>
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        
        holder.binding.txtTitle.text = video.name
        
        val sizeMB = video.size / (1024 * 1024)
        val durationMin = (video.duration / 1000) / 60
        val durationSec = (video.duration / 1000) % 60
        holder.binding.txtSizeDuration.text = String.format("%02d:%02d • %d MB", durationMin, durationSec, sizeMB)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra("VIDEO_URI", video.uri)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = videoList.size
}
