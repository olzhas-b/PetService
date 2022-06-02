/*
 * *
 *  * Created by Ali Ashkeyev on 30.05.2022, 22:29
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 30.05.2022, 22:29
 *
 */

package com.example.budka.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.budka.databinding.FragmentOtpSmsBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OtpSmsFragment: Fragment() {
    private var _viewBinding: FragmentOtpSmsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationId: String? = null
    private val args: OtpSmsFragmentArgs by navArgs()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentOtpSmsBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verificationId = args.verificationId
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Invalid request
                        Toast.makeText(requireContext(), "Ошибочный запрос", Toast.LENGTH_LONG)
                            .show()

                    }
                    is FirebaseTooManyRequestsException -> {
                        // The SMS quota for the project has been exceeded
                        Toast.makeText(requireContext(), "Лимит СМС превышен", Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onCodeSent(
                newVerificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(newVerificationId, p1)
                verificationId = newVerificationId
                Toast.makeText(
                    requireContext(),
                    "Отправлен новый код для подтверждения",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
            setListeners()
    }

    fun setListeners(){
        with(viewBinding) {
            inputCode1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(!TextUtils.isEmpty(p0) && p0?.length==6){
                        verifyUser(p0.toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            resendTv.setOnClickListener {
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(args.phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }

    private fun verifyUser(code: String) {
        if (verificationId!=null){
            viewBinding.progressBar.visibility = View.VISIBLE
            val phoneAuthCredential = PhoneAuthProvider
                .getCredential(verificationId!!, code)
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener { task ->
                    viewBinding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Yes", Toast.LENGTH_LONG)
                            .show()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Невенрный код подтверждения",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
        }

    }
    }

