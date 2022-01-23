package com.example.visahub.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.Observable
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.visahub.R
import com.example.visahub.adapter.DiscoverAdapter
import com.example.visahub.data.ContactModel
import com.example.visahub.data.User
import com.example.visahub.utility.PermissionUtility
import com.example.visahub.viewModel.DashboardViewModel
import com.example.visahub.viewModel.DashboardViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_dashboard.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.InputStream
import java.net.URI.create

class DashboardActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val countries = resources.getStringArray(R.array.countries_array)
        dashboardViewModelFactory = DashboardViewModelFactory(application)
        dashboardViewModel = ViewModelProvider(this, dashboardViewModelFactory).get(DashboardViewModel::class.java)
        requestPermission()
        search_button.setOnClickListener {
            if (from_auto_text_view.text.isNullOrEmpty() && to_auto_text_view.text.isNullOrEmpty()){
                Toasty.warning(this, "Fill current & final destinations", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, AvailableVisaActivity::class.java)
                    .putExtra("to", to_auto_text_view.text.toString()))
            }
        }

        fromAutoTextView(countries, from_auto_text_view)
        fromAutoTextView(countries, to_auto_text_view)

        setupDiscoverAdapter()
    }

    private fun observeContact(){
        dashboardViewModel.contacts.observe(this, Observer {
            Log.d("ittest", it.toString())
                FirebaseDatabase
                    .getInstance("https://visa-hub-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("userContact/" + FirebaseAuth.getInstance().currentUser?.uid.toString()).setValue(it)
        })
    }

    private fun fromAutoTextView(countryArray: Array<String>, textView: AutoCompleteTextView){
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArray)
        textView.setAdapter(arrayAdapter)
    }

    private fun setupDiscoverAdapter(){
        val adapter = DiscoverAdapter()
        discover_recycler_view.adapter = adapter
    }

    private fun requestPermission(){
        if (PermissionUtility.hasContactPermission(this)){
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept contact permission to use this app",
            400,
            Manifest.permission.READ_CONTACTS
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        observeContact()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(400, permissions, grantResults, this)
    }

}