/*
 * *
 *  * Created by Ali Ashkeyev on 30.05.2022, 21:22
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 30.05.2022, 21:22
 *
 */

package com.example.budka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budka.databinding.FragmentOtpPhoneBinding
import com.example.budka.databinding.FragmentUpdateProfileBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OtpPhoneFragment: Fragment() {
    private var _viewBinding: FragmentOtpPhoneBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentOtpPhoneBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                viewBinding.progressBar.visibility = View.GONE
                viewBinding.confirmButton.visibility = View.VISIBLE
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                viewBinding.progressBar.visibility = View.GONE
                viewBinding.confirmButton.visibility = View.VISIBLE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(requireContext(), "Ошибочный запрос", Toast.LENGTH_LONG).show()

                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(requireContext(), "Лимит СМС превышен", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, p1)
                findNavController().navigate(OtpPhoneFragmentDirections
                    .actionOtpPhoneFragmentToOtpSmsFragment
                        (
                        verificationId,
                        viewBinding.phoneNumber.text.toString().trim()
                    )
                )
            }
        }
        setListeners()
    }

    fun setListeners(){
        with(viewBinding) {
            confirmButton.setOnClickListener {
                if (phoneNumber.text.toString().trim().isEmpty()){
                    Toast.makeText(requireContext(), "Введите номер для подтвержденя", Toast.LENGTH_LONG).show()
                }
                else {
                    progressBar.visibility = View.VISIBLE
                    confirmButton.visibility = View.GONE
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber.text.toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
            }
        }
    }


}