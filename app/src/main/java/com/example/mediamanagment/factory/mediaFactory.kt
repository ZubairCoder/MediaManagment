package com.example.mediamanagment.factory

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mediamanagment.repository.mediaRepository
import com.example.vmexternalstorage.viewModel.ImagesViewModel

class mediaFactory(val mediaRepository: mediaRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImagesViewModel(application = Application(),mediaRepository) as T
    }
}