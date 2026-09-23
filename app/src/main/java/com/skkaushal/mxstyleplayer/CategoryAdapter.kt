package com.skkaushal.mxstyleplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.model.CategoryItem

class CategoryAdapter(
    private val categoryList: List<CategoryItem>,
    private val onItemClick: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCategoryName)
        val txtCount: TextView = itemView.findViewById(R.id.txtCategoryCount)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryList[position]
        holder.txtName.text = item.name
        holder.txtCount.text = "${item.count} Songs"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}
