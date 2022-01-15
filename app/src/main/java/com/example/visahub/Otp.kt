package com.example.visahub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_otp.*

class Otp : AppCompatActivity() {

    private var name: String ?= null
    private var phone: String ?= null
    private var email: String ?= null

    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        intent.apply {
            name = getStringExtra("name")
            phone = getStringExtra("phone")
            email = getStringExtra("email")
        }
        mobile_number_text.text = "+91$phone"

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        authenticationViewModel.sendVerificationCode("+91$phone!!", this)


        login_btn.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        resend_code.setOnClickListener {
            authenticationViewModel.resendVerificationCode("+91$phone", this)
        }
    }
}