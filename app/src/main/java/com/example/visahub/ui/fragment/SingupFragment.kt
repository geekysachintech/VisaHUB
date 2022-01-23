package com.example.visahub.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.visahub.*
import com.example.visahub.data.User
import com.example.visahub.ui.DashboardActivity
import com.example.visahub.utility.ResponseState
import com.example.visahub.viewModel.AuthenticationViewModel
import com.example.visahub.viewModel.GoogleSignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_s_ingup.view.already_register_text
import kotlinx.android.synthetic.main.fragment_s_ingup.view.continue_btn
import kotlinx.android.synthetic.main.fragment_s_ingup.view.email_edit_text
import kotlinx.android.synthetic.main.fragment_s_ingup.view.google_btn
import kotlinx.android.synthetic.main.fragment_s_ingup.view.name_edit_text
import kotlinx.android.synthetic.main.fragment_s_ingup.view.phone_edit_text

class SingupFragment : Fragment() {

    private lateinit var authenticationViewModel: AuthenticationViewModel
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInViewModel: GoogleSignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_s_ingup, container, false)
        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        googleSignInViewModel = ViewModelProvider(this).get(GoogleSignInViewModel::class.java)
        initGoogleSignInClient()
        view.continue_btn.setOnClickListener {
            navigateToOtpScreen(
                view.name_edit_text.text.toString(),
                view.phone_edit_text.text.toString(),
                view.email_edit_text.text.toString(),
                view
            )
        }

        view.google_btn.setOnClickListener {
            signInUsingGoogle()
        }

        view.already_register_text.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_singupFragment_to_loginFragment)
        }
        return view
    }

    private fun spannableText(textView: MaterialTextView, start: Int, end: Int){
        val ss = SpannableString("")
        val clickableSpan =  object: ClickableSpan(){
            override fun onClick(widget: View) {
                startActivity(Intent())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
            }
        }

        ss.setSpan(clickableSpan, 7, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

//        SpannableString ss = new SpannableString("Android is a Software stack");
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                startActivity(new Intent(MyActivity.this, NextActivity.class));
//            }
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        };
//        ss.setSpan(clickableSpan, 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        TextView textView = (TextView) findViewById(R.id.hello);
//        textView.setText(ss);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        textView.setHighlightColor(Color.TRANSPARENT);
    }

    private fun navigateToOtpScreen(name: String, phone: String, email: String , view:View){
        if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()){
            if (phone.length != 10){
                Toasty.warning(requireActivity(), "Phone No should be 10 digits.", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle()
                val user = User(
                    name = name,
                    email = email,
                    mobileNo = phone
                )
                bundle.putSerializable("user", user)
                Navigation.findNavController(view).navigate(R.id.action_singupFragment_to_otpFragment, bundle)
            }
        } else {
            Toasty.warning(requireActivity(),"Please fill all 3 fields properly", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1000867469023-4na0hm951kfgus04l294b1tadklko8pk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInUsingGoogle() {
        val signInGoogleIntent = googleSignInClient.signInIntent
        startActivityForResult(signInGoogleIntent, 300)
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
                    startActivity(
                        Intent(requireActivity(), DashboardActivity::class.java))
                    activity?.finish()
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toasty.error(requireActivity(), "Login failed: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGoogleAuthCredential(account: GoogleSignInAccount) {
        //binding.progressBar.visible()
        val googleTokeId = account.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokeId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
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

}