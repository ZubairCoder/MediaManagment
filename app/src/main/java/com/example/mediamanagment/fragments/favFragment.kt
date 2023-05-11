package com.example.mediamanagment.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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
import com.example.mediamanagment.adopter.FavAdopter
import com.example.mediamanagment.databinding.FragmentFavBinding
import com.example.mediamanagment.db.BookMark
import com.example.mediamanagment.factory.mediaFactory
import com.example.mediamanagment.interfaces.onItemClick
import com.example.mediamanagment.repository.mediaRepository
import com.example.viewmodel.ui.db.AppDatabase
import com.example.vmexternalstorage.viewModel.ImagesViewModel


class favFragment : Fragment(),onItemClick {
    private var binding : FragmentFavBinding? = null
    private var recyclerView : RecyclerView? = null
    private var imagesViewModel : ImagesViewModel? = null
    val PICK_PDF_FILE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavBinding.inflate(LayoutInflater.from(context),container,false)
        // Inflate the layout for this fragment
        recyclerView = binding?.recylerView
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val db = AppDatabase.getDatabase(requireContext())
        val mediaRepository = mediaRepository(requireContext(),db)

        imagesViewModel= ViewModelProvider(this,mediaFactory(mediaRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.users?.observe(viewLifecycleOwner, Observer {
            recyclerView?.adapter = FavAdopter(requireContext(),it,this)
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
        TODO("Not yet implemented")
    }

    override fun onPdfMenuClick(files: Files) {
        TODO("Not yet implemented")
    }

    override fun onFavClick(bookMark: BookMark) {
        imagesViewModel?.deleteData(bookMark)
    }

    override fun onFavVideoClick(bookMark: BookMark) {


        if (bookMark.title.endsWith("jpg")){
            val builder = AlertDialog.Builder(requireContext())
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.show_imgdailogue, null)
            builder.setView(view)
            builder.show()
            val title = view.findViewById<TextView>(R.id.imgTxtView)
            val img = view.findViewById<ImageView>(R.id.ViewimageView)
            val btnPopMore = view.findViewById<ImageView>(R.id.btnMore)
            title.text = bookMark.title
            val imagesUri= bookMark.url
            val imagesTitle = bookMark.title
            Glide.with(requireContext()).load(bookMark.url).thumbnail(0.1f).centerCrop().into(img)
           // Toast.makeText(requireContext(), "this is image", Toast.LENGTH_SHORT).show()
        }
        else if (bookMark.url.endsWith("pdf")){
            try {
                val open = Intent(Intent.ACTION_VIEW)
                open.type = "application/pdf"
                val path = bookMark.url
                open.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
                startActivity(Intent.createChooser(open, "Open with..."))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(requireContext(), "pdf", Toast.LENGTH_SHORT).show()
        }
        else{
            val playerFragment = ExoplayerFragment.newInstance(bookMark.url)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fagContainerView,playerFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
/*        if (bookMark.title.endsWith("mp4")){
            val playerFragment = ExoplayerFragment.newInstance(bookMark.url)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fagContainerView,playerFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        else if (bookMark.title.endsWith("jpg")){
            val builder = AlertDialog.Builder(requireContext())
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.show_imgdailogue, null)
            builder.setView(view)
            builder.show()
            val title = view.findViewById<TextView>(R.id.imgTxtView)
            val img = view.findViewById<ImageView>(R.id.ViewimageView)
            val btnPopMore = view.findViewById<ImageView>(R.id.btnMore)
            title.text = bookMark.title
            val imagesUri= bookMark.url
            val imagesTitle = bookMark.title
            //val imageUri = images.imageUri
            Glide.with(requireContext()).load(bookMark.url).thumbnail(0.1f).centerCrop().into(img)
            Toast.makeText(requireContext(), "this is image", Toast.LENGTH_SHORT).show()
        }
        else if (bookMark.title.endsWith("mp3")){
                val playerFragment = ExoplayerFragment.newInstance(bookMark.url)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fagContainerView,playerFragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
        else if (bookMark.title.endsWith("acc")){
            Toast.makeText(requireContext(), "Audio", Toast.LENGTH_SHORT).show()
        }
        else{

           *//* val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            val pdfFile = File(bookMark.url)
            pdfIntent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf")
            pdfIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY

            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            context?.startActivity(pdfIntent)*//*
            Toast.makeText(requireContext(), "pdf", Toast.LENGTH_SHORT).show()
        }

   *//* private fun openPDF(fileName: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        File(
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download",
            "NSIT - Notices"
        )
        val myPDF: File = File(
            Environment.getExternalStorageDirectory() + "/Download/NSIT - Notices",
            "$fileName.pdf"
        )
        val uri: Uri =
            FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", myPDF)
       // Log.d(TAG, "openPDF: intent with uri: $uri")
        intent.setDataAndType(uri, "application/pdf")
        requireContext().startActivity(Intent.createChooser(intent, "Open with..."))*/
    }
    fun openFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, PICK_PDF_FILE)
    }
}