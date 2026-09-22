package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.databinding.ItemFolderBinding
import com.skkaushal.mxstyleplayer.model.FolderItem

class FolderAdapter(
    private val folderList: List<FolderItem>,
    private val onFolderClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folderList[position]

        holder.binding.txtFolderName.text = folder.folderName
        holder.binding.txtVideoCount.text = "${folder.videoList.size} videos"

        holder.itemView.setOnClickListener {
            onFolderClick(folder)
        }
    }

    override fun getItemCount(): Int = folderList.size
}
