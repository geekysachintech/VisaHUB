package com.example.visahub.repository

import com.google.firebase.auth.PhoneAuthProvider
import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.visahub.data.User
import com.example.visahub.data.VisaDetails
import com.example.visahub.utility.ResponseState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.TimeUnit
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.*

class AuthenticationRepository {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val databaseReference: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance("https://visa-hub-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }

    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var mCredentials = MutableLiveData<PhoneAuthCredential>()
    var mVerificationId = MutableLiveData<String>()
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken ?= null
    val authenticatedUserMutableLiveData: MutableLiveData<ResponseState<User>> = MutableLiveData()

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
                authenticatedUserMutableLiveData.value = ResponseState.Error(p0.localizedMessage)
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

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, user: User) : MutableLiveData<ResponseState<User>>{
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            val phone = firebaseAuth.currentUser?.phoneNumber
            authenticatedUserMutableLiveData.value = ResponseState.Success(user)
            Log.d("TAG" , "Login Successful $phone" )
            registerUserWithDatabase(user, firebaseAuth.currentUser!!.uid)
        }.addOnFailureListener {
            Log.d("TAG" , it.toString())
            authenticatedUserMutableLiveData.value = ResponseState.Error(it.localizedMessage)
        }
        return authenticatedUserMutableLiveData
    }

    fun signInWithGoogle(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1000867469023-4na0hm951kfgus04l294b1tadklko8pk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun registerUserWithDatabase(user: User, key: String){
        databaseReference.getReference("userData").child(key).setValue(user)
    }

   /* fun checkUserExistsOrNot(user: User) : MutableLiveData<ResponseState<User>>{
        val isUserAlreadyLogin: MutableLiveData<ResponseState<User>> = MutableLiveData()
        val mRef = databaseReference.getReference("userData")
        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children){
                    val detail  = snap.getValue(User::class.java)
                    if (user.mobileNo == detail!!.mobileNo){
                        isUserAlreadyLogin.value = ResponseState.SignUpMessage("User Already Exist. Please try using sign in")
                    }
                    Log.d("userdataexists", detail?.email.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        return isUserAlreadyLogin
    }*/
}