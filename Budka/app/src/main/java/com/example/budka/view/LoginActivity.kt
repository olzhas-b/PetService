/*
 * *
 *  * Created by Ali Ashkeyev on 07.01.22 15:37
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 07.01.22 15:37
 *
 */

package com.example.budka.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.budka.R
import com.example.budka.data.model.Statesealed
import com.example.budka.viewModel.SignInViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity: AppCompatActivity() {

    private lateinit var wrongDataText: TextView
    private lateinit var signInButton: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: ProgressBar
    private val signInViewModel: SignInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInProcessing()
        bindViews()
    }

    private fun bindViews() {
        username = findViewById(R.id.evUsername)
        password = findViewById(R.id.evPassword)
        signInButton = findViewById(R.id.btnSignIn)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        wrongDataText = findViewById(R.id.tvWrongData)

        signInButton.setOnClickListener {
            signInViewModel.signIn(username.text.toString(), password.text.toString())
        }
    }

    private fun signInProcessing() {
        signInViewModel.state.observe(this, Observer { result ->
            when(result) {

                is Statesealed.ShowLoading ->{
                    progressBar.visibility = View.VISIBLE
                }

                is Statesealed.HideLoading ->{
                    progressBar.visibility = View.GONE
                }

                is Statesealed.Result ->{
                    val intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                }

                is Statesealed.FailedLoading ->{
                    signInViewModel.message.observe(this, Observer { message ->
                        wrongDataText.text = message
                    })
                }

            }
        })
    }
}