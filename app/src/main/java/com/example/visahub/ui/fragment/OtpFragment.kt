package com.example.visahub.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.visahub.viewModel.AuthenticationViewModel
import com.example.visahub.ui.DashboardActivity
import com.example.visahub.R
import com.example.visahub.data.User
import com.example.visahub.utility.ResponseState
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.view.*

class OtpFragment : Fragment() {

    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_otp, container, false)
        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        user = arguments?.getSerializable("user") as User
        authenticationViewModel.sendVerificationCode("+91" + user.mobileNo, requireActivity())
        if (user.mobileNo?.isNotEmpty() == true){
            Toasty.success(requireContext(), "Otp has been send to " + user.mobileNo, Toast.LENGTH_SHORT).show()
        }
        view.validate_btn.setOnClickListener {
            validateOtp(user)
        }

        view.resend_code.setOnClickListener {
            authenticationViewModel.resendVerificationCode("+91" + user.mobileNo, requireActivity())
        }
        return view
    }

    private fun validateOtp(user: User){
        val otp: String = et1.text.toString() +
                et2.text.toString() +
                et3.text.toString() +
                et4.text.toString() +
                et5.text.toString() +
                et6.text.toString()

        if (otp.length != 6) {
            Toasty.error(requireContext(), "Please fill all field", Toast.LENGTH_SHORT).show()
        } else {
            authenticationViewModel.signInWithPhoneCredentials(otp, user)
            signInSuccessFailure()
        }
    }

    private fun signInSuccessFailure(){
        authenticationViewModel.authenticateUserLiveData.observe(viewLifecycleOwner, { authenticatedUser ->
            when(authenticatedUser) {
                is ResponseState.Error -> {
                    authenticatedUser.message ?.let {
                        Toasty.error(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                is ResponseState.Success -> {
                    Toasty.success(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(activity , DashboardActivity::class.java))
                }
                is ResponseState.Loading -> {
                }
            }
        })
    }

}