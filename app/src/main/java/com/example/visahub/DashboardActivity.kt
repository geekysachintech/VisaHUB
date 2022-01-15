package com.example.visahub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.visahub.adapter.DiscoverAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val countries = resources.getStringArray(R.array.countries_array)
        search_button.setOnClickListener {
            startActivity(Intent(this, VisaDisplayActivity::class.java))
        }
        fromAutoTextView(countries, from_auto_text_view)
        fromAutoTextView(countries, to_auto_text_view)
        setupDiscoverAdapter()
    }

    private fun fromAutoTextView(countryArray: Array<String>, textView: AutoCompleteTextView){
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArray)
        textView.setAdapter(arrayAdapter)
    }

    private fun setupDiscoverAdapter(){
        val adapter = DiscoverAdapter(this)
        discover_recycler_view.adapter = adapter
    }
}