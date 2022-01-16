package com.example.visahub.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.visahub.AuthenticationViewModel
import com.example.visahub.DashboardActivity
import com.example.visahub.R
import kotlinx.android.synthetic.main.fragment_otp.view.*

class OtpFragment : Fragment() {

    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_otp, container, false)
        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        view.validate_btn.setOnClickListener {
            startActivity(Intent(activity , DashboardActivity::class.java))
        }

        return view
    }

}