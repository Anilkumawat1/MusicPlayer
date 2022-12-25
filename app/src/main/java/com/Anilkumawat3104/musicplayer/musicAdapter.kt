package com.Anilkumawat3104.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class musicAdapter(val context : Context, val name : ArrayList<music>) : RecyclerView.Adapter<musicAdapter.MyviewHolder>(){

    var onItemClick : ((String)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.music_view,parent,false)
        return MyviewHolder(view)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        holder.song_name.text = name[position].title
        holder.song_album.text = name[position].album
        holder.song_duration.text = formatDuration(name[position].duration)
        Glide.with(context)
            .load(name[position].image)
            .apply(RequestOptions().placeholder(R.drawable.flash)).centerCrop()
            .into(holder.images)
        holder.itemView.setOnClickListener{
            val intent = Intent(context,Activity_player::class.java)
            intent.putExtra("Index",position)
            intent.putExtra("class","musicAdapter")
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return name.size
    }
    class MyviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var song_name= itemView.findViewById<TextView>(R.id.song_name)
        var song_album= itemView.findViewById<TextView>(R.id.song_album)
        var song_duration= itemView.findViewById<TextView>(R.id.song_duration)
        var images = itemView.findViewById<ShapeableImageView>(R.id.song_image)
    }
}