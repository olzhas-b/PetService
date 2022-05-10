/*
 * *
 *  * Created by Ali Ashkeyev on 17.03.2022, 0:13
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 17.03.2022, 0:13
 *
 */

package com.example.budka.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.budka.data.model.SignUpData
import com.example.budka.data.model.doIfFailure
import com.example.budka.data.model.doIfLoading
import com.example.budka.data.model.doIfSuccess
import com.example.budka.databinding.ActivityRegistrationBinding
import com.example.budka.viewModel.SignInViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RegistrationActivity: AppCompatActivity() {
    private val signInViewModel: SignInViewModel by viewModel()
    private lateinit var viewBinding: ActivityRegistrationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.btnSignIn.setOnClickListener {
            val registrationData = SignUpData(
                username = viewBinding.evLogin.text.toString(),
                login = viewBinding.evLogin.text.toString(),
                phone = viewBinding.evphone.text.toString(),
                password = viewBinding.evPassword.text.toString(),
                firstName = viewBinding.evFirstname.text.toString(),
                lastName = viewBinding.evLastName.text.toString(),
                fullName = viewBinding.evFirstname.text.toString() +" "+viewBinding.evLastName.text.toString()
            )
            signInViewModel.register(registrationData).observe(
                this, { result ->
                    result.doIfSuccess {
                        viewBinding.progressBar.visibility = View.GONE
                        val intent = Intent(this, LoginActivity::class.java)
                        this.startActivity(intent)
                    }
                    result.doIfFailure { error, data ->
                        viewBinding.progressBar.visibility = View.GONE
                    }
                    result.doIfLoading {
                        if (it) {
                            viewBinding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            )
        }
    }
}