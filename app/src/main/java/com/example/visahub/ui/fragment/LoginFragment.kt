package com.example.visahub.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.visahub.R
import com.example.visahub.data.User
import com.example.visahub.ui.DashboardActivity
import com.example.visahub.utility.ResponseState
import com.example.visahub.viewModel.GoogleSignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_loginfragment.*
import kotlinx.android.synthetic.main.fragment_loginfragment.view.*


class LoginFragment : Fragment() {

    private lateinit var googleSignInViewModel: GoogleSignInViewModel
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loginfragment, container, false)
        googleSignInViewModel = ViewModelProvider(this).get(GoogleSignInViewModel::class.java)
        initGoogleSignInClient()

        view.login_btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_otpFragment)
        }

        view.new_user_text.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_singupFragment)
        }

        view.login_btn.setOnClickListener {
            navigateToOtpScreen(mobile_number.text.toString(), view)
        }

        view.google_btn.setOnClickListener {
            signInUsingGoogle()
        }
        return view
    }

    private fun navigateToOtpScreen(phone:
                                    String, view:View){
        if (phone.isNotEmpty()){
            if (phone.length != 10){
                Toasty.warning(requireActivity(), "Phone No should be 10 digits.", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle()
                val user = User(
                    mobileNo = phone
                )
                bundle.putSerializable("user", user)
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_otpFragment, bundle)
            }
        } else {
            Toasty.warning(requireActivity(),"Please fill phone number", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getGoogleAuthCredential(account: GoogleSignInAccount) {
        //binding.progressBar.visible()
        val googleTokeId = account.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokeId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInUsingGoogle() {
        val signInGoogleIntent = googleSignInClient.signInIntent
        startActivityForResult(signInGoogleIntent, 300)
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1000867469023-4na0hm951kfgus04l294b1tadklko8pk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        googleSignInViewModel.signInWithGoogle(googleAuthCredential)
        googleSignInViewModel.authenticateUserLiveData.observe(viewLifecycleOwner, { authenticatedUser ->
            when(authenticatedUser) {
                is ResponseState.Error -> {
                    authenticatedUser.message ?.let {
                        Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                        //context?.toast(it)
                    }
                }
                is ResponseState.Success -> {
                    if (authenticatedUser.data != null){

                    }
                    //update ui
                }
                is ResponseState.Loading -> {
                    //show progress
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent? ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java) !!
                if (account != null) {
                    getGoogleAuthCredential(account)
                    startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toasty.error(requireActivity(), "Login failed: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
}