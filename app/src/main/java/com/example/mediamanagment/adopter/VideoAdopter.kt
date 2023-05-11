package com.example.mediamanagment.adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.databinding.RecyclerLayoutBinding
import com.example.mediamanagment.databinding.VideoRecyclerBinding
import com.example.mediamanagment.interfaces.onItemClick
import com.example.vmexternalstorage.viewModel.ImagesViewModel

class VideoAdopter (val context: Context, var data : List<Videos>, val imagesViewModel: ImagesViewModel, val onItemClick: onItemClick) : RecyclerView.Adapter<VideoAdopter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdopter.MyViewHolder {
        val binding = VideoRecyclerBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdopter.MyViewHolder, position: Int) {
        val data = data[position]
        holder.binding.imgTxt.text = data.title
        //val path = File(data.url)
        Glide.with(context).load(data.url.toString()).thumbnail(0.1f).centerCrop().into(holder.binding.imageView)
        holder.binding.imageView.setOnClickListener {
            onItemClick.onVideoClick(data)
        }
        holder.binding.btnMore.setOnClickListener {
            onItemClick.onvideoMenuClick(data)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }
    class MyViewHolder(var binding: VideoRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}