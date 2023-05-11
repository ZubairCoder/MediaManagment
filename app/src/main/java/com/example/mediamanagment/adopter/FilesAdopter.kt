package com.example.mediamanagment.adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.R
import com.example.mediamanagment.databinding.AudioRecyclerBinding
import com.example.mediamanagment.interfaces.onItemClick

class FilesAdopter (val context: Context, var data : List<Files>, val onItemClick: onItemClick) : RecyclerView.Adapter<FilesAdopter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesAdopter.MyViewHolder {
        val binding = AudioRecyclerBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilesAdopter.MyViewHolder, position: Int) {
        val data = data[position]
        holder.binding.imgTxt.text = data.title
        holder.binding.imageView.setImageResource(R.drawable.picture_as_pdf)

       holder.itemView.setOnClickListener {
           onItemClick.onPdfClick(data)
       }
        holder.binding.btnMore.setOnClickListener {
            onItemClick.onPdfMenuClick(data)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(var binding: AudioRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
