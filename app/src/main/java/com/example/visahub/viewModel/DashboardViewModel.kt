package com.example.visahub.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.visahub.data.ContactModel
import com.example.visahub.repository.ContactRepository

class DashboardViewModel(
    context: Application,
    private val contactRepository: ContactRepository
) : AndroidViewModel(context){
    var contacts: LiveData<List<ContactModel>> = liveData {
        emit(contactRepository.fetchContacts())
    }
}