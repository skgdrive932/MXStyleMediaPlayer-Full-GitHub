package com.skkaushal.mxstyleplayer.util
import android.content.Context
import android.provider.MediaStore
import com.skkaushal.mxstyleplayer.model.VideoItem

object MediaStoreRepository {
    fun videos(context:Context):List<VideoItem>{
        val out=mutableListOf<VideoItem>()
        val p=arrayOf(MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION,MediaStore.Video.Media.SIZE)
        context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,p,null,null,
            MediaStore.Video.Media.DATE_ADDED+" DESC")?.use { c ->
            val n=c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val id=c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val d=c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val s=c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            while(c.moveToNext()){
                val uri=android.content.ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,c.getLong(id))
                out+=VideoItem(c.getString(n),uri.toString(),c.getLong(d),c.getLong(s))
            }
        }
        return out
    }
}
