package com.example.mediamanagment.interfaces

import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.db.BookMark

interface onItemClick {
    fun onClick(images: Images)
    fun onAudioClick(audios: Audios)
    fun onAudioMenuClick(audios: Audios)
    fun onVideoClick(videos: Videos)
    fun onvideoMenuClick(videos: Videos)
    fun onPdfClick(files: Files)
    fun onPdfMenuClick(files: Files)
    fun onFavClick(bookMark: BookMark)
    fun onFavVideoClick(bookMark: BookMark)
}