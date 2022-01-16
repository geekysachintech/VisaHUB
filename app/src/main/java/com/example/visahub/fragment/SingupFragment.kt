package com.example.visahub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.visahub.AuthenticationViewModel
import com.example.visahub.R
import kotlinx.android.synthetic.main.fragment_s_ingup.view.already_register_text
import kotlinx.android.synthetic.main.fragment_s_ingup.view.continue_btn
import kotlinx.android.synthetic.main.fragment_s_ingup.view.email_edit_text
import kotlinx.android.synthetic.main.fragment_s_ingup.view.google_btn
import kotlinx.android.synthetic.main.fragment_s_ingup.view.name_edit_text
import kotlinx.android.synthetic.main.fragment_s_ingup.view.phone_edit_text

class SingupFragment : Fragment() {

    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_s_ingup, container, false)
        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        view.continue_btn.setOnClickListener {
            navigateToOtpScreen(
                view.name_edit_text.text.toString(),
                view.phone_edit_text.text.toString(),
                view.email_edit_text.text.toString(),
                view
            )
        }

        view.google_btn.setOnClickListener {
            googleSignIn()
        }

        view.already_register_text.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_singupFragment_to_loginFragment)
        }

        return view
    }

    private fun googleSignIn(){
        startActivityForResult(authenticationViewModel.googleSignInClient(requireActivity()).signInIntent, 200)
    }

    private fun navigateToOtpScreen(name: String, phone: String, email: String , view:View){
        if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()){
            Navigation.findNavController(view).navigate(R.id.action_singupFragment_to_otpFragment)
        } else {
            Toast.makeText(activity,"Please fill all 3 fields properly", Toast.LENGTH_SHORT).show()
        }

    }

}