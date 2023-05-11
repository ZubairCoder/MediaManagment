package com.example.mediamanagment.fragments

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.R
import com.example.mediamanagment.adopter.VideoAdopter
import com.example.mediamanagment.databinding.FragmentVideoBinding
import com.example.mediamanagment.db.BookMark
import com.example.mediamanagment.factory.mediaFactory
import com.example.mediamanagment.interfaces.onItemClick
import com.example.mediamanagment.repository.mediaRepository
import com.example.viewmodel.ui.db.AppDatabase
import com.example.vmexternalstorage.viewModel.ImagesViewModel

class videoFragment : Fragment(), onItemClick {
    private var binding : FragmentVideoBinding? = null
    private var recyclerView: RecyclerView? = null
    private var imagesViewModel: ImagesViewModel? = null
    private var popupMenu: PopupMenu? = null
    private var videoUri: String? = null
    private var videoTitle: String? = null
    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentVideoBinding.inflate(LayoutInflater.from(context),container,false)
        // Inflate the layout for this fragment
        recyclerView = binding?.recylerView

        val db = AppDatabase.getDatabase(requireContext())
        val imageRepository = mediaRepository(requireContext(),db)
        imagesViewModel =
            ViewModelProvider(this, mediaFactory(imageRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.getVideos()
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        imagesViewModel?.getVideoLiveData()?.observe(requireActivity(), Observer {
            recyclerView?.adapter = VideoAdopter(requireContext(),it , imagesViewModel!!, this)
        })



        return binding?.root
    }

    override fun onClick(images: Images) {
        TODO("Not yet implemented")
    }

    override fun onAudioClick(audios: Audios) {
        TODO("Not yet implemented")
    }

    override fun onAudioMenuClick(audios: Audios) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onVideoClick(videos: Videos) {

   val playerFragment = ExoplayerFragment.newInstance(videos.url)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fagContainerView,playerFragment)
            ?.addToBackStack(null)
            ?.commit()

/*
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.show_imgdailogue, null)
        builder.setView(view)
        builder.show()
        val title = view.findViewById<TextView>(R.id.imgTxtView)
        val img = view.findViewById<ImageView>(R.id.ViewimageView)
        val btnPopMore = view.findViewById<ImageView>(R.id.btnMore)
        title.text = videos.title
        imagesUri= videos.url
        imagesTitle = videos.title
        Glide.with(requireContext()).load(videos.url).thumbnail(0.1f).centerCrop().into(img)

        btnPopMore.setOnClickListener {
            popUpMenu(btnPopMore)
        }*/

    }

    override fun onvideoMenuClick(videos: Videos) {
        val btnOption = view?.findViewById<ImageView>(R.id.btnMore)
        videoTitle = videos.title
        videoUri = videos.url
        popUpMenu(btnOption!!)
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
                        imagesViewModel?.insertData(videoTitle!!,videoUri!!)
                        //  Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                R.id.delete -> {
                    delete(videoUri!!.toUri())
                   // Toast.makeText(requireContext(), "Delete is not implemented", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.share -> {
                    try {
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/*"
//                        val path = imagesUri?.let { it1 -> File(it1).absolutePath }
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoUri))
                        startActivity(Intent.createChooser(share, "Share via"))
                        //Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
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
                    titleRename.setText(videoTitle)
                    btnRename.setOnClickListener {
                        alert.dismiss()
                    }
                    true
                }
                R.id.move -> {
                    Toast.makeText(
                        requireContext(),
                        "Move is not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
        popupMenu!!.show()
    }
    fun rename(uri: Uri?, rename: String?) {

        //create content values with new name and update
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, rename)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireContext().getContentResolver().update(uri!!, contentValues, null)
        }
    }
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
}