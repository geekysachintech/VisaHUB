package com.example.visahub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.visahub.repository.DatabaseRepository
import com.example.visahub.data.VisaDetails

class AvailableVisaViewModel : ViewModel() {

    private val databaseRepository = DatabaseRepository()
    val visaDetailsList : LiveData<ArrayList<VisaDetails>> = databaseRepository.visaDetails

    fun getVisaListByCountry(country: String){
        databaseRepository.getListVisaData(country)
    }
}