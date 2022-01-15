package com.example.visahub

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class AuthenticationViewModel : ViewModel() {

    var authenticationRepository = AuthenticationRepository()
    var credential:  LiveData<PhoneAuthCredential>?= null
    var verificationId: LiveData<String> ?= null


    fun sendVerificationCode(phoneNumber: String , activity: Activity) {
        authenticationRepository.callbackSetup()
        authenticationRepository.sendVerificationCode(phoneNumber , activity)
        credential = authenticationRepository.mCredentials
    }

    fun signInWithPhoneCredentials(code: String) {
        verificationId = authenticationRepository.mVerificationId
        authenticationRepository.signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId?.value!! , code))
    }

    fun resendVerificationCode(phoneNumber: String, activity: Activity){
        authenticationRepository.resendVerificationCode(phoneNumber, activity)
    }

    fun googleSignInClient(activity: Activity) : GoogleSignInClient {
        return authenticationRepository.signInWithGoogle(activity)
    }
}