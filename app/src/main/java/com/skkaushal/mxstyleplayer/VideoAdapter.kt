package com.skkaushal.mxstyleplayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skkaushal.mxstyleplayer.databinding.ItemVideoBinding
import com.skkaushal.mxstyleplayer.model.VideoItem
class VideoAdapter(private val items:List<VideoItem>,private val click:(VideoItem)->Unit):RecyclerView.Adapter<VideoAdapter.VH>(){
 class VH(val b:ItemVideoBinding):RecyclerView.ViewHolder(b.root)
 override fun onCreateViewHolder(p:ViewGroup,v:Int)=VH(ItemVideoBinding.inflate(LayoutInflater.from(p.context),p,false))
 override fun getItemCount()=items.size
 override fun onBindViewHolder(h:VH,i:Int){val x=items[i];h.b.name.text=x.name;h.b.meta.text="${x.duration/60000}:${(x.duration/1000%60).toString().padStart(2,'0')}  •  ${x.size/1048576} MB";h.itemView.setOnClickListener{click(x)}}
}
