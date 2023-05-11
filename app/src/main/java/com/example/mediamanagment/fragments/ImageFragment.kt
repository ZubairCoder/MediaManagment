package com.example.mediamanagment.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.R
import com.example.mediamanagment.databinding.FragmentImageBinding
import com.example.mediamanagment.db.BookMark
import com.example.mediamanagment.factory.mediaFactory
import com.example.mediamanagment.interfaces.onItemClick
import com.example.mediamanagment.repository.mediaRepository
import com.example.viewmodel.ui.db.AppDatabase
import com.example.vmexternalstorage.adopter.MyAdopter
import com.example.vmexternalstorage.viewModel.ImagesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class ImageFragment : Fragment(), onItemClick {
    private var binding : FragmentImageBinding? = null
    private var recyclerView: RecyclerView? = null
    private var imagesViewModel: ImagesViewModel? = null
    private var popupMenu: PopupMenu? = null
    private var imagesUri: String? = null
    private var imagesTitle: String? = null
    private var imageUri: Uri? = null
    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null
    private var images : Images? = null
    private var clickedImages : Images? = null
    private  var xCoOrdinate : Float? = null
    private  var yCoOrdinate : Float? = null

    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 1
        private const val DELETE_PERMISSION_REQUEST = 2
    }
    private var pendingDeleteImage: Images? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentImageBinding.inflate(LayoutInflater.from(context),container,false)
        // Inflate the layout for this fragment
        recyclerView = binding?.recylerView
        val db = AppDatabase.getDatabase(requireContext())
        val imageRepository = mediaRepository(requireContext(),db)
        imagesViewModel =
            ViewModelProvider(this, mediaFactory(imageRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.getImages()
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        imagesViewModel!!.data.observe(requireActivity(), Observer { imgList ->
            recyclerView?.adapter = MyAdopter(requireContext(), imgList, imagesViewModel!!, this)
        })
        ////************permission delete*************
        imagesViewModel?.permissionNeededForDelete?.observe(requireActivity(), Observer { intentSender ->
            intentSender?.let {
                startIntentSenderForResult(
                    intentSender,
                    DELETE_PERMISSION_REQUEST,
                    null,
                    0,
                    0,
                    0,
                    null
                )
            }
        })




        launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){data ->
            if (data.resultCode == RESULT_OK){
                Toast.makeText(requireContext(),"Permission granted!",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),"Permission denied!",Toast.LENGTH_SHORT).show()
            }
        }

        return binding?.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == DELETE_PERMISSION_REQUEST) {
            imagesViewModel?.deletePendingImage()
        }
    }

    override fun onClick(images: Images) {
        clickedImages = images
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.show_imgdailogue, null)
        builder.setView(view)
        builder.show()
        val title = view.findViewById<TextView>(R.id.imgTxtView)
        val img = view.findViewById<ImageView>(R.id.ViewimageView)
        val btnPopMore = view.findViewById<ImageView>(R.id.btnMore)
        title.text = images.title
        imagesUri= images.url
        imagesTitle = images.title
        imageUri = images.imageUri
        Glide.with(requireContext()).load(images.url).thumbnail(0.1f).centerCrop().into(img)
        //Toast.makeText(requireContext(), "$imagesUri", Toast.LENGTH_SHORT).show()
        btnPopMore.setOnClickListener {
            popUpMenu(btnPopMore)
        }
    }

    override fun onAudioClick(audios: Audios) {
        TODO("Not yet implemented")
    }

    override fun onAudioMenuClick(audios: Audios) {
        TODO("Not yet implemented")
    }

    override fun onVideoClick(videos: Videos) {
        TODO("Not yet implemented")
    }

    override fun onvideoMenuClick(videos: Videos) {
        TODO("Not yet implemented")
    }

    override fun onPdfClick(files: Files) {
        TODO("Not yet implemented")
    }

    override fun onPdfMenuClick(files: Files) {
        TODO("Not yet implemented")
    }

    override fun onFavClick(bookMark: BookMark) {
        TODO("Not yet implemented")
    }

    override fun onFavVideoClick(bookMark: BookMark) {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingInflatedId")
    private fun popUpMenu(imageView: View) {
        popupMenu = PopupMenu(imageView.context, imageView)
        popupMenu!!.inflate(R.menu.show_menu)
        popupMenu!!.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.favorite -> {
                    try {
                       imagesViewModel?.insertData(imagesTitle!!,imagesUri!!)
                        //  Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }

                R.id.delete -> {
                    deleteImage(clickedImages!!)

                   // delete(imageUri!!)
                   // binding?.recylerView?.adapter?.notifyDataSetChanged()
                    //binding?.recylerView?.adapter?.notifyItemRemoved(id)
                    true
                }
                R.id.share -> {
                    try {
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/*"
//                        val path = imagesUri?.let { it1 -> File(it1).absolutePath }
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagesUri))
                        startActivity(Intent.createChooser(share, "Share via"))
                        //  Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                R.id.rename -> {
                    val builder = AlertDialog.Builder(requireContext())
                    val view = LayoutInflater.from(requireContext()).inflate(R.layout.rename_dailogue, null)
                    builder.setView(view)
                    val alert = builder.show()
                    alert.create()
                    val titleRename = view.findViewById<EditText>(R.id.txtRename)
                    val btnRename = view.findViewById<Button>(R.id.btnRename)
                    titleRename.setText(imagesTitle)
                    btnRename.setOnClickListener {
                        val currenFile = File(imagesUri)
                        // val newName = bindingRF.renameField.text
                        if (titleRename != null && currenFile.exists() && titleRename.toString().isNotEmpty()
                        ) {

                            val newFile = File(currenFile.parentFile, titleRename.toString() + "." + currenFile.extension
                            )
                           // Log.d("IsFileRename", "popUpMenu: $titleRename")

                            if (currenFile.renameTo(newFile)) {
                                Toast.makeText(context, titleRename.toString(), Toast.LENGTH_SHORT).show()
                                MediaScannerConnection.scanFile(context, arrayOf(newFile.toString()), arrayOf("pics"), null)
                                images?.title = newFile.name
                                imagesUri = newFile.path
                            } else {
                                Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else {
                            Toast.makeText(context, "Access Denied", Toast.LENGTH_SHORT).show()
                        }

                      // Rename(imagesUri!!)
                        alert.dismiss()
                        binding?.recylerView?.adapter?.notifyDataSetChanged()
                    }
                   // Toast.makeText(requireContext(), "Edit is not implemented", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.move -> {
                    moveImage(imageUri.toString())
                    //Toast.makeText(requireContext(), "Detail is not implemented", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
        popupMenu!!.show()
    }

        fun Rename(txtrename: String) {


            val currenFile = File(imagesUri)
            // val newName = bindingRF.renameField.text
            if (txtrename != null && currenFile.exists() && txtrename.toString()
                    .isNotEmpty()
            ) {

                val newFile = File(
                    currenFile.parentFile, txtrename.toString() + "." + currenFile.extension
                )
                Log.d("IsFileRename", "popUpMenu: $txtrename")

                if (currenFile.renameTo(newFile)) {
                    Toast.makeText(context, txtrename.toString(), Toast.LENGTH_SHORT).show()
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(newFile.toString()),
                        arrayOf("image/*"),
                        null
                    )
                    images?.title = newFile.name
                    imagesUri = newFile.path
                } else {
                    Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(context, "Access Denied", Toast.LENGTH_SHORT).show()
            }
        }

/*
        //create content values with new name and update
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, rename)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireContext().getContentResolver().update(uri!!, contentValues, null)
        }*/
//    }

    fun delete(uri: Uri) {
        val contentResolver: ContentResolver = requireContext().contentResolver
        try {

            //delete object using resolver
            contentResolver.delete(uri, null, null)
        } catch (e: SecurityException) {
            var pendingIntent: PendingIntent? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val collection: ArrayList<Uri> = ArrayList()
                collection.add(uri)
                pendingIntent = MediaStore.createDeleteRequest(contentResolver, collection)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                //if exception is recoverable then again send delete request using intent
                if (e is RecoverableSecurityException) {
                    pendingIntent = e.userAction.actionIntent
                }
            }
            if (pendingIntent != null) {
                val sender = pendingIntent.intentSender
                val request = IntentSenderRequest.Builder(sender).build()
                launcher?.launch(request)
            }
        }
    }
    private fun moveImage(imagePath: String) {
        val imageFile = File(imagePath)
        val newImageFile = File(context?.getExternalFilesDir(null), imageFile.name)
        imageFile.copyTo(newImageFile, true)
        imageFile.delete()
    }
    fun onMove(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                xCoOrdinate = view.x - event.rawX
                yCoOrdinate = view.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> view.animate().x(event.rawX + xCoOrdinate!!)
                .y(event.rawY + yCoOrdinate!!).setDuration(0).start()
            else -> return false
        }
        return true
    }
     fun deleteImage(images: Images) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_dialog_title)
            .setMessage(getString(R.string.delete_dialog_message, images.title))
            .setPositiveButton(R.string.delete_dialog_positive) { _: DialogInterface, _: Int ->
                imagesViewModel?.deleteImage(images)
            }
            .setNegativeButton(R.string.delete_dialog_negative) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }



}