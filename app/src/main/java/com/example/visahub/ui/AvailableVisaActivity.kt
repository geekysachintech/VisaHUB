package com.example.visahub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.visahub.R
import com.example.visahub.adapter.AvailableVisaAdapter
import com.example.visahub.viewModel.AvailableVisaViewModel
import kotlinx.android.synthetic.main.activity_available_visa.*

class AvailableVisaActivity : AppCompatActivity() {

    private lateinit var availableVisaViewModel: AvailableVisaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_orange)
        setContentView(R.layout.activity_available_visa)
        val to = intent.getStringExtra("to")
        availableVisaViewModel = ViewModelProvider(this).get(AvailableVisaViewModel::class.java)
        Log.d("finalCheck", to!!)
        back_button.setOnClickListener {
            onBackPressed()
        }
        destination_tv.text = to
        availableVisaViewModel.getVisaListByCountry(to.toLowerCase())
        observingVisaDetailsList()
    }

    private fun observingVisaDetailsList(){
        availableVisaViewModel.visaDetailsList.observe(this) {
            Log.d("finalCheck", it.toString())
            available_visa_recyclerView.adapter = AvailableVisaAdapter(it)
        }
    }
}