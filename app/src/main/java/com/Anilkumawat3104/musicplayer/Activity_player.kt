package com.Anilkumawat3104.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class Activity_player : AppCompatActivity() {
    companion object {
        lateinit var musicListp: ArrayList<music>
        var position: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var isPlaying: Boolean = false
    }
        private lateinit var imageicon : ImageView
        private lateinit var prev : ImageView
        private lateinit var next : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        setContentView(R.layout.activity_player)

        imageicon = findViewById(R.id.play_pause)
        prev = findViewById(R.id.previous_song)
        next = findViewById(R.id.next_song)
        imageicon = findViewById(R.id.play_pause)
        initializeLayout()

        imageicon.setOnClickListener{
             if(isPlaying)
                 pauseMusic()
            else
                playMusic()
        }
        prev.setOnClickListener{
         prevNext(false)
        }
        next.setOnClickListener{
         prevNext(true)
        }
    }
    private fun setlayout(){
        Glide.with(this)
            .load(musicListp[position].image)
            .apply(RequestOptions().placeholder(R.drawable.flash)).centerCrop()
            .into(findViewById<ShapeableImageView>(R.id.songImage))
        findViewById<TextView>(R.id.song_name1).text = musicListp[position].title
        findViewById<TextView>(R.id.song_duration).text = formatDuration(musicListp[position].duration)
    }
    private fun createMediaPlayer(){
        try{
            if(mediaPlayer==null)
                mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListp[position].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            imageicon.setImageResource(R.drawable.pause_icon)
        }
        catch (e : Exception){
            return
        }
    }
    private fun initializeLayout(){
        position = intent.getIntExtra("Index",0)
        when(intent.getStringExtra("class")){
            "musicAdapter" -> {
                musicListp = ArrayList()
                musicListp.addAll(MainActivity.musicList)
                setlayout()
                createMediaPlayer()
            }
            "MainActivity" -> {
                musicListp = ArrayList()
                musicListp.addAll(MainActivity.musicList)
                musicListp.shuffle()
                setlayout()
                createMediaPlayer()

            }
        }
    }
    private fun playMusic(){
         imageicon.setImageResource(R.drawable.pause_icon)
        isPlaying = true
        mediaPlayer!!.start()
    }
    private fun pauseMusic(){
        imageicon.setImageResource(R.drawable.play_icon)
        isPlaying = false
        mediaPlayer!!.pause()
    }
    private fun prevNext(incre : Boolean){
        if(incre){
            position++
            if(position < musicListp.size){
                setlayout()
                createMediaPlayer()
            }
            else{
                position = 0
                setlayout()
                createMediaPlayer()
            }
        }
        else{
            position--
            if(position >= 0){
                setlayout()
                createMediaPlayer()
            }
            else{
                position = musicListp.size-1
                setlayout()
                createMediaPlayer()
            }
        }
    }
}