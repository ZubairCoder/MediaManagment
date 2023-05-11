package com.example.mediamanagment.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamanagment.DataModels.Audios
import com.example.mediamanagment.DataModels.Files
import com.example.mediamanagment.DataModels.Images
import com.example.mediamanagment.DataModels.Videos
import com.example.mediamanagment.R
import com.example.mediamanagment.adopter.FilesAdopter
import com.example.mediamanagment.databinding.FragmentFilesBinding
import com.example.mediamanagment.db.BookMark
import com.example.mediamanagment.factory.mediaFactory
import com.example.mediamanagment.interfaces.onItemClick
import com.example.mediamanagment.repository.mediaRepository
import com.example.viewmodel.ui.db.AppDatabase
import com.example.vmexternalstorage.viewModel.ImagesViewModel
import com.google.android.exoplayer2.text.span.TextAnnotation.Position
import java.io.File


class FilesFragment : Fragment(), onItemClick {
    private var binding:FragmentFilesBinding? = null
    private var recyclerView: RecyclerView? = null
    private var imagesViewModel: ImagesViewModel? = null
    private var popupMenu: PopupMenu? = null
    private var fileUri: String? = null
    private var fileTitle: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFilesBinding.inflate(LayoutInflater.from(context), container, false)
        // Inflate the layout for this fragment
        recyclerView = binding?.recylerView
        val db = AppDatabase.getDatabase(requireContext())
        val imageRepository = mediaRepository(requireContext(),db)
        imagesViewModel =
            ViewModelProvider(this, mediaFactory(imageRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.getFiles()
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        imagesViewModel?.getFilesLiveData()?.observe(requireActivity(), Observer {
            recyclerView?.adapter = FilesAdopter(requireContext(), it, this)
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

    override fun onVideoClick(videos: Videos) {
        TODO("Not yet implemented")
    }

    override fun onvideoMenuClick(videos: Videos) {
        TODO("Not yet implemented")
    }

    override fun onPdfClick(files: Files) {
        Toast.makeText(requireContext(), files.url, Toast.LENGTH_SHORT).show()
        try{
            val open = Intent(Intent.ACTION_VIEW)
            open.type = "application/pdf"
            val path = files.url
            open.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
            startActivity(Intent.createChooser(open, "Open with..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPdfMenuClick(files: Files) {
        val btnOption = view?.findViewById<ImageView>(R.id.btnMore)
        fileTitle = files.title
        fileUri = files.url
        popUpMenu(btnOption!!)
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
                        imagesViewModel?.insertData(fileTitle!!,fileUri!!)
                        //  Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                R.id.delete -> {
                    Toast.makeText(
                        requireContext(),
                        "Delete is not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.share -> {
                    try {
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/*"
//                        val path = imagesUri?.let { it1 -> File(it1).absolutePath }
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileUri))
                        startActivity(Intent.createChooser(share, "Share via"))
                        //                       Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                R.id.rename -> {
                    val builder = AlertDialog.Builder(requireContext())
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.rename_dailogue, null)
                    builder.setView(view)
                    val alert = builder.show()
                    alert.create()
                    val titleRename = view.findViewById<EditText>(R.id.txtRename)
                    val btnRename = view.findViewById<Button>(R.id.btnRename)
                    titleRename.setText(fileTitle)
                    btnRename.setOnClickListener {
                        //******************
                        val currenFile = File(fileUri)
                        if (titleRename != null && currenFile.exists() && titleRename.toString()
                                .isNotEmpty()
                        ) {
                            val newFile = File(
                                currenFile.parentFile,
                                titleRename.toString() + "." + currenFile.extension
                            )
                            //Log.d("IsFileRename", "popUpMenu: $newName")

                            if (currenFile.renameTo(newFile)) {
                                Toast.makeText(requireContext(), titleRename.toString(), Toast.LENGTH_SHORT).show()
                                MediaScannerConnection.scanFile(
                                    requireContext(),
                                    arrayOf(newFile.toString()),
                                    arrayOf("pdf/*"),
                                    null
                                )
                                fileTitle = newFile.name
                                fileUri = newFile.path

                            } else {
                                Toast.makeText(requireContext(), "Error Occurred", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else {
                            Toast.makeText(requireContext(), "Access Denied", Toast.LENGTH_SHORT).show()
                        }

                        alert.dismiss()
                    }
                   // Toast.makeText(requireContext(), "Edit is not implemented", Toast.LENGTH_SHORT).show()
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
}