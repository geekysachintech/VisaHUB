package com.example.visahub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.visahub.R
import com.example.visahub.data.VisaDetails
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var details: VisaDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_orange)

        details = intent.getSerializableExtra("visaDetail") as VisaDetails
        Log.d("Details" , details.toString())
        bindData()

    }

    private fun bindData() {
        c_toolbar_layout.title = details.type.toString()
        about_text.text = details.about.toString()
        qualification_text.text = details.qualificaton.toString().replace("\\n" ,"\n" )
        other_text.text = details.other.toString()
        duration_text.text = details.processingTime.toString()
        Glide
            .with(this)
            .load(details.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.circle_profile)
            .error(R.drawable.circle_profile)
            .into(image_view);
    }
}