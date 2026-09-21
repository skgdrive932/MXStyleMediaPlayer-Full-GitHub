package com.skkaushal.mxstyleplayer
import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.skkaushal.mxstyleplayer.databinding.ActivityMainBinding
import com.skkaushal.mxstyleplayer.util.MediaStoreRepository

class MainActivity:AppCompatActivity(){
    private lateinit var b:ActivityMainBinding
    private val permission=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){}
    override fun onCreate(s:Bundle?){
        super.onCreate(s); b=ActivityMainBinding.inflate(layoutInflater); setContentView(b.root)
        val p=mutableListOf<String>()
        if(Build.VERSION.SDK_INT>=33){p+=Manifest.permission.READ_MEDIA_VIDEO;p+=Manifest.permission.READ_MEDIA_AUDIO;p+=Manifest.permission.POST_NOTIFICATIONS}
        if(p.isNotEmpty()) permission.launch(p.toTypedArray())
        load()
        b.about.setOnClickListener{ android.widget.Toast.makeText(this,"Sk.Kaushal\nskgdrive932@gmail.com\n+919779371866",android.widget.Toast.LENGTH_LONG).show() }
    }
    private fun load(){
        val list=MediaStoreRepository.videos(this)
        b.list.layoutManager=LinearLayoutManager(this)
        b.list.adapter=VideoAdapter(list){ startActivity(Intent(this,PlayerActivity::class.java).putExtra("uri",it.uri).putExtra("name",it.name)) }
        b.count.text="${list.size} videos"
    }
}
