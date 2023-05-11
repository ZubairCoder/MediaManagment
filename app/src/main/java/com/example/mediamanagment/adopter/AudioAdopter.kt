package com.example.mediamanagment.adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.R
import com.example.mediamanagment.databinding.AudioRecyclerBinding
import com.example.mediamanagment.databinding.RecyclerLayoutBinding
import com.example.mediamanagment.interfaces.onItemClick
import com.example.vmexternalstorage.viewModel.ImagesViewModel

class AudioAdopter(val context: Context, var data : List<Audios>, val onItemClick: onItemClick) : RecyclerView.Adapter<AudioAdopter.MyViewHolder>() {
    var imagesViewModel : ImagesViewModel? = null
    var audioUri: String? = null
    var audioTitle: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioAdopter.MyViewHolder {
        val binding = AudioRecyclerBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioAdopter.MyViewHolder, position: Int) {
        val data = data[position]
        holder.binding.imgTxt.text = data.title
        holder.binding.imageView.setImageResource(R.drawable.audio_file)
        //val path = File(data.url)
//       Glide.with(context).load(data.url.toString()).thumbnail(0.1f).centerCrop().into(holder.binding.imageView)
        holder.binding.audioRecycler.setOnClickListener {
            onItemClick.onAudioClick(data)
        }
        holder.binding.btnMore.setOnClickListener {
            onItemClick.onAudioMenuClick(data)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    class MyViewHolder(var binding: AudioRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {


    }

   /*    private fun popUpMenu(imageView: View) {
        val popupMenu = PopupMenu(imageView.context,imageView)
        popupMenu.inflate(R.menu.show_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.favorite -> {
                    try {
                        Toast.makeText(context, "Data Inserted", Toast.LENGTH_SHORT).show()
                        imagesViewModel?.insertData(audioTitle!!,audioUri!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                else ->false
            }
        }
        popupMenu.show()
    }*/

}