package com.example.visahub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.visahub.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_loginfragment.view.*


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loginfragment, container, false)

        view.login_btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_otpFragment)
        }

        view.new_user_text.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_singupFragment)
        }
        return view
    }
}