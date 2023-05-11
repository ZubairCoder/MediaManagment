package com.example.vmexternalstorage.adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.databinding.RecyclerLayoutBinding
import com.example.mediamanagment.interfaces.onItemClick
import com.example.vmexternalstorage.viewModel.ImagesViewModel

class MyAdopter(val context: Context, var data : List<Images>, val imagesViewModel: ImagesViewModel, val onItemClick: onItemClick) : RecyclerView.Adapter<MyAdopter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdopter.MyViewHolder {
        val binding = RecyclerLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyAdopter.MyViewHolder, position: Int) {
        val data = data[position]
        holder.binding.imgTxt.text = data.title
        //val path = File(data.url)
        Glide.with(context).load(data.url.toString()).thumbnail(0.1f).centerCrop().into(holder.binding.imageView)
        holder.itemView.setOnClickListener {
            onItemClick.onClick(data)

            // for popUp menu in adopter
           // popUpMenu(holder.binding.imageView)
        }
    }

/*    private fun popUpMenu(imageView: View) {
        val popupMenu = PopupMenu(imageView.context,imageView)
        popupMenu.inflate(R.menu.show_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete -> {true}
                else ->{false}
            }
        }
        popupMenu.show()
    }*/

    override fun getItemCount(): Int {
        return data.size
    }
    class MyViewHolder(var binding: RecyclerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}