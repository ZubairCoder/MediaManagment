package com.example.mediamanagment.repository

import android.annotation.SuppressLint
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.util.getColumnIndex
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.db.BookMark
import com.example.viewmodel.ui.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class mediaRepository(val context: Context,private val appDatabase: AppDatabase) {

    //      *******Images***********
val imagesLiveData = MutableLiveData<List<Images>>()
fun getImages() {
  val images = mutableListOf<Images>()

          val cursor = context.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // Iterate through cursor to get music file information
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageId)
                images.add(Images(title,image,imageUri))
            } while (cursor.moveToNext())
            cursor.close()
        }

  /*  val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID)
    val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
    val selcetionArgs = arrayOf("pics")
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selcetionArgs,
        sortOrder
    )
    if (cursor != null && cursor.moveToFirst()) {
        do {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageId)
            images.add(Images(title,image,imageUri))
        } while (cursor.moveToNext())
        cursor.close()
    }*/

    imagesLiveData.postValue(images)
}

fun getImagesLiveData(): LiveData<List<Images>> {
    return imagesLiveData
}


    //*****Audios************

    val audiLiveData = MutableLiveData<List<Audios>>()
    @SuppressLint("Range")
    fun getAudio() {
        val audio = mutableListOf<Audios>()
        // Use content resolver to query for music files on device
        val musicCursor = context.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // Iterate through cursor to get music file information
        if (musicCursor != null && musicCursor.moveToFirst())  {
            do {
                val title =
                    musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
               // val musicArtist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
               // val musicAlbum =musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC))
                val path = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))

                // Create Music object and add to list
               audio.add(Audios(title,path))
            }while (musicCursor.moveToNext())
            musicCursor.close()
        }
        audiLiveData.postValue(audio)
    }
    fun getAudioLiveData(): LiveData<List<Audios>> {
        return audiLiveData
    }

    //********Videos*********

    val videoLiveData = MutableLiveData<List<Videos>>()
    @SuppressLint("Range")
    fun getVideos() {
       val videos = mutableListOf<Videos>()
    /*    val projection = arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA)
        val selection = "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} = ?"
        val selcetionArgs = arrayOf("Videos")
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selcetionArgs,
            sortOrder
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videos.add(Videos(title,image))
            } while (cursor.moveToNext())
            cursor.close()
        }*/


        // Use content resolver to query for music files on device
        val musicCursor = context.contentResolver?.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // Iterate through cursor to get music file information
        if (musicCursor != null && musicCursor.moveToFirst())  {
            do {
                val title =
                    musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                // val musicArtist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Video.Media.ARTIST))
                // val musicAlbum =musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Video.Media.IS_MUSIC))
                val path = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Video.Media.DATA))

                // Create Music object and add to list
                videos.add(Videos(title,path))
            }while (musicCursor.moveToNext())
            musicCursor.close()
        }
        videoLiveData.postValue(videos)
    }
    fun getVideosLiveData(): LiveData<List<Videos>> {
        return videoLiveData
    }

                //*********Files*****************
     val fileLiveData = MutableLiveData<List<Files>>()
/*    @SuppressLint("Range")
    fun getFiles() {
        val docFile = mutableListOf<Files>()
        // Use content resolver to query for music files on device
        val musicCursor = context.contentResolver?.query(
            MediaStore.MediaColumns.,
            null,
            null,
            null,
            null
        )

        // Iterate through cursor to get music file information
        if (musicCursor != null && musicCursor.moveToFirst())  {
            do {
                val title =
                    musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE))
                // val musicArtist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Files.FileColumns.ARTIST))
                val path = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))

                // Create Music object and add to list
                docFile.add(Files(title,path))
            }while (musicCursor.moveToNext())
            musicCursor.close()
        }
        fileLiveData.postValue(docFile)
    }
    fun getFilesLiveData(): LiveData<List<Files>> {
        return fileLiveData
    }*/

    // Fetch PDF files from device's download folder
    @SuppressLint("Range")
    fun getPdfFiles() {
        val pdfFilesList = mutableListOf<Files>()

/*        val fileCursor = context.contentResolver?.query(
            MediaStore.Files.getContentUri("external"),
            null,
            null,
            null,
            null
        )

        // Iterate through cursor to get music file information
        if (fileCursor != null && fileCursor.moveToFirst()) {
            do {
                val title =
                    fileCursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val path =
                    fileCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

                // Create Music object and add to list
                pdfFilesList.add(Files(title.toString(), path.toString()))
            }while (fileCursor.moveToNext())
            fileCursor.close()
        }*/

/*        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        ActivityResultContracts.StartActivityForResult()*/


        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // Check if downloads folder exists
        if (downloadsFolder.exists() && downloadsFolder.isDirectory) {
            // Get list of files in downloads folder
            val files = downloadsFolder.listFiles()
            // Iterate through the files and filter PDF files
            files?.forEach { file ->
                if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) {
                    // Add PDF file to list
                    pdfFilesList.add(Files(file.name, file.path))
                }
            }
            Log.d("pdfFilesChecking", "getPdfFiles: ${files!!.size} ")
        }
        // Update LiveData with PDF files list
        fileLiveData.postValue(pdfFilesList)
    }
    fun getFilesLiveData(): LiveData<List<Files>> {
        return fileLiveData
    }

    //*********db for bookMark*****************

    fun getAllData(): LiveData<List<BookMark>> {
        return  appDatabase.dataDao().getAllData()
    }

    suspend fun insertData(bookMark: BookMark){
        appDatabase.dataDao().dataInsert(bookMark)
    }
    suspend fun updateData(bookMark: BookMark){
        appDatabase.dataDao().updateData(bookMark)
    }
    suspend fun delete(bookMark: BookMark){
        appDatabase.dataDao().deleteData(bookMark)
    }

    //*********Delete with permission*****************

    private var pendingDeleteImage: Images? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete
    suspend fun performDeleteImage(images: Images) {
        withContext(Dispatchers.IO) {
            val contentResolver: ContentResolver = context.contentResolver
            try {
                contentResolver.delete(images.imageUri,
                    "${MediaStore.Images.Media._ID} = ?",
                    null)
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
        }
    }

}