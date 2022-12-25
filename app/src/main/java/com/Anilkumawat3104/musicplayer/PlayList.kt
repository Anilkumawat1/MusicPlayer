package com.Anilkumawat3104.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlayList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        setContentView(R.layout.activity_play_list)
    }
}