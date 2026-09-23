package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.FolderItem

class FolderAdapter(
    private val folderList: List<FolderItem>,
    private val onItemClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFolderName: TextView = itemView.findViewById(R.id.txtFolderName)
        val txtVideoCount: TextView = itemView.findViewById(R.id.txtVideoCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folderList[position]
        holder.txtFolderName.text = folder.folderName
        holder.txtVideoCount.text = "${folder.videoList.size} Videos"

        holder.itemView.setOnClickListener {
            onItemClick(folder)
        }
    }

    override fun getItemCount(): Int = folderList.size
}
