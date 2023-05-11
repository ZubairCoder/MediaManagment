package com.example.mediamanagment.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.R
import com.example.mediamanagment.adopter.AudioAdopter
import com.example.mediamanagment.databinding.FragmentAudioBinding
import com.example.mediamanagment.databinding.FragmentImageBinding
import com.example.mediamanagment.db.BookMark
import com.example.mediamanagment.factory.mediaFactory
import com.example.mediamanagment.interfaces.onItemClick
import com.example.mediamanagment.repository.mediaRepository
import com.example.viewmodel.ui.db.AppDatabase
import com.example.vmexternalstorage.adopter.MyAdopter
import com.example.vmexternalstorage.viewModel.ImagesViewModel
import java.io.IOException

class audioFragment : Fragment(), onItemClick {
    private var binding : FragmentAudioBinding? = null
    private var recyclerView: RecyclerView? = null
    private var imagesViewModel: ImagesViewModel? = null
    private var popupMenu: PopupMenu? = null
    private var audioUri: String? = null
    private var audioTitle: String? = null
    private var mediaPlayer: MediaPlayer? = null





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAudioBinding.inflate(LayoutInflater.from(context),container,false)
        // Inflate the layout for this fragment
        recyclerView = binding?.recylerView
        val db = AppDatabase.getDatabase(requireContext())
        val imageRepository = mediaRepository(requireContext(),db)
        imagesViewModel =
            ViewModelProvider(this, mediaFactory(imageRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.getAudio()
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        imagesViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
            recyclerView?.adapter = AudioAdopter(requireContext(), it,this)
        })
        mediaPlayer = MediaPlayer()



        return binding?.root
    }

    override fun onClick(images: Images) {
    }

    override fun onAudioClick(audios: Audios) {

        try {
            val playerFragment = ExoplayerFragment.newInstance(audios.url)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fagContainerView,playerFragment)
                ?.addToBackStack(null)
                ?.commit()
        }catch (e :IOException ){
            e.printStackTrace()
        }

    }

    override fun onAudioMenuClick(audios: Audios) {
        val btnOption = view?.findViewById<ImageView>(R.id.btnMore)
        audioTitle = audios.title
        audioUri = audios.url
        popUpMenu(btnOption!!)
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
                        imagesViewModel?.insertData(audioTitle!!,audioUri!!)
                        //  Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                R.id.delete -> {
                    Toast.makeText(requireContext(), "Delete is not implemented", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.share -> {
                    try {
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/*"
//                        val path = imagesUri?.let { it1 -> File(it1).absolutePath }
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(audioUri))
                        startActivity(Intent.createChooser(share, "Share via"))
                        //                       Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
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
                    titleRename.setText(audioTitle)
                    btnRename.setOnClickListener {
                        alert.dismiss()
                    }
                    Toast.makeText(requireContext(), "Edit is not implemented", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.move -> {
                    Toast.makeText(
                        requireContext(),
                        "Detail is not implemented",
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
}