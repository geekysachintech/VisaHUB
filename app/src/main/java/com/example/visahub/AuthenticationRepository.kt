package com.example.visahub

import com.google.firebase.auth.PhoneAuthProvider
import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.TimeUnit
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class AuthenticationRepository {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var mCredentials = MutableLiveData<PhoneAuthCredential>()
    var mVerificationId = MutableLiveData<String>()
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken ?= null

    fun sendVerificationCode(phoneNumber: String , activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L , TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun callbackSetup() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                mCredentials.value = credential
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("TAG" , p0.toString())
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId.value = verificationId
                forceResendingToken = token
            }
        }
    }

    fun resendVerificationCode(phoneNumber: String , activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L , TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallback)
            .setForceResendingToken(forceResendingToken!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            val phone = firebaseAuth.currentUser?.phoneNumber
            Log.d("TAG" , "Login Successful $phone" )
        }.addOnFailureListener {
            Log.d("TAG" , it.toString())
        }
    }

    fun signInWithGoogle(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1000867469023-4na0hm951kfgus04l294b1tadklko8pk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

}