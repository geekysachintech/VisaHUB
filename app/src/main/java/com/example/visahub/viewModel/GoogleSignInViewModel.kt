package com.example.visahub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.visahub.repository.GoogleSignInRepository
import com.example.visahub.data.User
import com.example.visahub.utility.ResponseState
import com.google.firebase.auth.AuthCredential

class GoogleSignInViewModel : ViewModel() {

    private val authRepository = GoogleSignInRepository()

    private var _authenticateUserLiveData: MutableLiveData<ResponseState<User>> = MutableLiveData()
    val authenticateUserLiveData: LiveData<ResponseState<User>> get() = _authenticateUserLiveData

    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        _authenticateUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
    }

}