package com.example.visahub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        continue_btn.setOnClickListener {
            navigateToOtpScreen(
                name_edit_text.text.toString(),
                phone_edit_text.text.toString(),
                email_edit_text.text.toString()
            )
        }

        google_btn.setOnClickListener {
            googleSignIn()
        }
    }

    private fun googleSignIn(){
        startActivityForResult(authenticationViewModel.googleSignInClient(this).signInIntent, 200)
    }

    private fun navigateToOtpScreen(name: String, phone: String, email: String){
        if (!name.isNullOrEmpty() && !phone.isNullOrEmpty() && !email.isNullOrEmpty()){
            startActivity(Intent(this, Otp::class.java).apply {
                putExtra("name", name)
                putExtra("phone", phone)
                putExtra("email", email)
            })
        } else {
            Toast.makeText(this,"Please fill all 3 fields properly", Toast.LENGTH_SHORT).show()
        }

    }
}