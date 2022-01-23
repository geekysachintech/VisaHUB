package com.example.visahub.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
//        Log.d("Mobile" , user.mobileNo.toString())
        view.mobile_number_text.text = user.mobileNo.toString()
        authenticationViewModel.sendVerificationCode("+91" + user.mobileNo, requireActivity())
        if (user.mobileNo?.isNotEmpty() == true){
            Toasty.success(requireContext(), "Otp has been send to " + user.mobileNo, Toast.LENGTH_SHORT).show()
        }

        var editTextArray: Array<EditText> = arrayOf(view.et1 , view.et2 , view.et3 , view.et4 , view.et5 , view.et6)
        changeFocus(editTextArray)

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
                    startActivity(Intent(activity , DashboardActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK))

                }
                is ResponseState.Loading -> {
                }
            }
        })
    }

    /**
     * To pass all the edittext into the function to set text watcher on that
     */
    private fun changeFocus(editTextArray: Array<EditText>) {
        for (i in 0 until editTextArray.size - 1) {
            Log.d("tag" , i.toString())
            otpConcatenate(editTextArray[i] , editTextArray[i+1])
        }
    }

    /**
     * Function to add textWatcher to the editText and to change the focus of the edit text when we entered any value into it.
     */
    private fun otpConcatenate(currentEt: EditText, nextEt: EditText ) {
        currentEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().trim().isEmpty()) {
                    nextEt.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

}