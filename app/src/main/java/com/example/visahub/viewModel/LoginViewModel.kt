package com.example.visahub.viewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.visahub.repository.AuthenticationRepository
import com.example.visahub.data.User
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class LoginViewModel : ViewModel() {

    var authenticationRepository = AuthenticationRepository()
    var credential:  LiveData<PhoneAuthCredential>?= null
    var verificationId: LiveData<String>?= null


    fun sendVerificationCode(phoneNumber: String , activity: Activity) {
        authenticationRepository.callbackSetup()
        authenticationRepository.sendVerificationCode(phoneNumber , activity)
        credential = authenticationRepository.mCredentials
    }

    fun signInWithPhoneCredentials(code: String, user: User) {
        verificationId = authenticationRepository.mVerificationId
        authenticationRepository.signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId?.value!! , code), user)
    }

    fun resendVerificationCode(phoneNumber: String, activity: Activity){
        authenticationRepository.resendVerificationCode(phoneNumber, activity)
    }
}