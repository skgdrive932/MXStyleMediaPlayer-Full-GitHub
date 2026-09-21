package com.skkaushal.mxstyleplayer
import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.skkaushal.mxstyleplayer.databinding.ActivityPlayerBinding
import kotlin.math.abs
class PlayerActivity:AppCompatActivity(){
 private lateinit var b:ActivityPlayerBinding
 private lateinit var player:ExoPlayer
 private var locked=false
 private var speed=1f
 private var downX=0f; private var downY=0f; private var downVol=0
 override fun onCreate(s:Bundle?){super.onCreate(s);window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);b=ActivityPlayerBinding.inflate(layoutInflater);setContentView(b.root)
  player=ExoPlayer.Builder(this).build();b.player.player=player
  val uri=Uri.parse(intent.getStringExtra("uri")); player.setMediaItem(MediaItem.fromUri(uri));player.prepare();player.play()
  b.play.setOnClickListener{if(player.isPlaying)player.pause()else player.play()}
  b.back.setOnClickListener{player.seekTo((player.currentPosition-10000).coerceAtLeast(0))}
  b.forward.setOnClickListener{player.seekTo(player.currentPosition+10000)}
  b.speed.setOnClickListener{speed=when(speed){1f->1.25f;1.25f->1.5f;1.5f->2f;else->1f};player.setPlaybackSpeed(speed);b.speed.text="${speed}x"}
  b.lock.setOnClickListener{locked=!locked;b.controls.alpha=if(locked)0.15f else 1f}
  b.full.setOnClickListener{requestedOrientation=if(resources.configuration.orientation==1)android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT}
  b.seek.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
   override fun onProgressChanged(s:SeekBar,p:Int,f:Boolean){if(f)player.seekTo(p.toLong())}
   override fun onStartTrackingTouch(s:SeekBar){}
   override fun onStopTrackingTouch(s:SeekBar){}
  })
  player.addListener(object:Player.Listener{override fun onPlaybackStateChanged(state:Int){b.seek.max=player.duration.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()}})
  b.player.setOnTouchListener{_,e->gesture(e);true}
 }
 private fun gesture(e:MotionEvent):Boolean{
  if(e.action==MotionEvent.ACTION_DOWN){downX=e.x;downY=e.y;downVol=audio.volume;return true}
  if(e.action==MotionEvent.ACTION_UP && !locked){
   val dx=e.x-downX;val dy=e.y-downY
   if(abs(dx)>120 && abs(dx)>abs(dy)){player.seekTo((player.currentPosition+(if(dx>0)10000 else -10000)).coerceAtLeast(0));return true}
   if(abs(dy)>80){if(downX< b.player.width/2) window.attributes=window.attributes.apply{screenBrightness=(screenBrightness-dy/b.player.height).coerceIn(0f,1f)}
    else audio.setVolume((downVol + (-dy/b.player.height*audio.maxVolume)).toInt().coerceIn(0,audio.maxVolume));return true}
  };return true
 }
 private val audio by lazy{getSystemService(AUDIO_SERVICE) as android.media.AudioManager}
 override fun onDestroy(){player.release();super.onDestroy()}
}
