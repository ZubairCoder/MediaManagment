package com.example.vmexternalstorage.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.Context
import android.content.IntentSender
import android.database.ContentObserver
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.repository.mediaRepository
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.db.BookMark
import kotlinx.coroutines.*

class ImagesViewModel( application: Application, private val mediaRepository: mediaRepository) : AndroidViewModel(application) {
        //***********Images***********
    val data: LiveData<List<Images>> = mediaRepository.imagesLiveData

    fun getImages() {
        mediaRepository.getImages()
    }

    fun getImagesLiveData(): LiveData<List<Images>> {
        mediaRepository.getImagesLiveData()
        return data
    }

    //******* Audio**************
    val audio: MutableLiveData<List<Audios>> = mediaRepository.audiLiveData

    fun getAudio() {
        mediaRepository.getAudio()
    }

    fun getAudioLiveData(): LiveData<List<Audios>> {
        mediaRepository.getAudioLiveData()
        return audio
    }

    //******* Videos**************

    val video: MutableLiveData<List<Videos>> = mediaRepository.videoLiveData

    fun getVideos() {
        mediaRepository.getVideos()
    }

    fun getVideoLiveData(): LiveData<List<Videos>> {
        mediaRepository.getVideosLiveData()
        return video
    }

    //******* Files**************

    val pdf: MutableLiveData<List<Files>> = mediaRepository.fileLiveData

    fun getFiles() {
        mediaRepository.getPdfFiles()
    }

    fun getFilesLiveData(): LiveData<List<Files>> {
        mediaRepository.getFilesLiveData()
        return pdf
    }


    //*********db for bookMark*****************

    val users: LiveData<List<BookMark>> = mediaRepository.getAllData()
    fun insertData(title :String, url :String) {
        GlobalScope.launch {
            val data = BookMark(null,title,url)
            mediaRepository.insertData(data)
        }
    }

    fun deleteData(bookMark: BookMark){
        GlobalScope.launch {
            mediaRepository.delete(bookMark)
        }
    }

    //*********Delete with permission*****************
    private var contentObserver: ContentObserver? = null
    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

    private var pendingDeleteImage: Images? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = mediaRepository.permissionNeededForDelete
  /*  suspend fun performDeleteImage(images: Images) {
        withContext(Dispatchers.IO) {
            try {

                getApplication<Application>().contentResolver.delete(
                    images.imageUri,
                    "${MediaStore.Images.Media._ID} = ?",
                    arrayOf(images.title.toString())
                )
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                        securityException as? RecoverableSecurityException
                            ?: throw securityException
                    pendingDeleteImage = images
                    _permissionNeededForDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    )
                } else {
                    throw securityException
                }
            }
            catch (e : Exception){
                Log.d("checkingException",e.message.toString())
            }
        }
    }*/

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteImage(images: Images) {
        GlobalScope.launch {
            mediaRepository.performDeleteImage(images)
        }
    }
    fun deletePendingImage() {
        pendingDeleteImage?.let { image ->
            pendingDeleteImage = null
            deleteImage(image)
        }
    }


}