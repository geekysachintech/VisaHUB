package com.example.visahub.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.visahub.repository.ContactDataSource
import com.example.visahub.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalArgumentException

class DashboardViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            val source = ContactDataSource(application.contentResolver)
            DashboardViewModel(application, ContactRepository(source, Dispatchers.IO)) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel Class")
    }
}