package com.example.visahub.viewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.visahub.repository.AuthenticationRepository
import com.example.visahub.data.User
import com.example.visahub.utility.ResponseState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class AuthenticationViewModel : ViewModel() {

    var authenticationRepository = AuthenticationRepository()
    var credential:  LiveData<PhoneAuthCredential>?= null
    var verificationId: LiveData<String> ?= null

    private var _authenticateUserLiveData: MutableLiveData<ResponseState<User>> = MutableLiveData()
    val authenticateUserLiveData: LiveData<ResponseState<User>> get() = _authenticateUserLiveData

    fun sendVerificationCode(phoneNumber: String , activity: Activity) {
        authenticationRepository.callbackSetup()
        authenticationRepository.sendVerificationCode(phoneNumber , activity)
        credential = authenticationRepository.mCredentials
    }

    fun signInWithPhoneCredentials(code: String, user: User) {
        verificationId = authenticationRepository.mVerificationId
        val phoneAuthProvider = PhoneAuthProvider.getCredential(verificationId?.value!! , code)
        _authenticateUserLiveData = authenticationRepository.signInWithPhoneAuthCredential(phoneAuthProvider, user)
    }

    fun resendVerificationCode(phoneNumber: String, activity: Activity){
        authenticationRepository.resendVerificationCode(phoneNumber, activity)
    }

    fun googleSignInClient(activity: Activity) : GoogleSignInClient {
        return authenticationRepository.signInWithGoogle(activity)
    }

}