package com.example.mediamanagment.DataModels

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class Images(var title : String, var url : String, val imageUri: Uri)
data class ImageUP(val id: Long, val displayName: String, val dateTaken: Date, val contentUri: Uri) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ImageUP>() {
            override fun areItemsTheSame(oldItem: ImageUP, newItem: ImageUP) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ImageUP, newItem: ImageUP) = oldItem == newItem
        }
    }
}