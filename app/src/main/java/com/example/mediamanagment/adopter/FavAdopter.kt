package com.example.mediamanagment.adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.R
import com.example.mediamanagment.databinding.FavLayoutBinding
import com.example.mediamanagment.db.BookMark
import com.example.mediamanagment.interfaces.onItemClick

class FavAdopter (val context: Context, var data : List<BookMark>,
                  val onItemClick: onItemClick) : RecyclerView.Adapter<FavAdopter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdopter.MyViewHolder {
        val binding = FavLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavAdopter.MyViewHolder, position: Int) {
        val data = data[position]

        holder.binding.imgTxt.text = data.title
        if(data.title.endsWith("pdf")){
            holder.binding.imageView.setImageResource(R.drawable.picture_as_pdf)
        }
        else if (data.url.endsWith("mp3") or (data.url.endsWith("acc"))){
            holder.binding.imageView.setImageResource(R.drawable.audio_file)

        }
        else {
            Glide.with(context).load(data.url).thumbnail(0.1f).centerCrop()
                .into(holder.binding.imageView)
        }
        holder.binding.btndelete.setOnClickListener {
            onItemClick.onFavClick(data)
        }
        holder.binding.favRecycler.setOnClickListener {
            onItemClick.onFavVideoClick(data)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(var binding: FavLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}