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
import com.example.budka.data.model.doIfFailure
import com.example.budka.data.model.doIfLoading
import com.example.budka.data.model.doIfSuccess
import com.example.budka.viewModel.SignInViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity: AppCompatActivity() {

    private lateinit var wrongDataText: TextView
    private lateinit var hint: TextView
    private lateinit var signInButton: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: ProgressBar
    private val signInViewModel: SignInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindViews()
    }

    private fun bindViews() {
        username = findViewById(R.id.evUsername)
        password = findViewById(R.id.evPassword)
        signInButton = findViewById(R.id.btnSignIn)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        wrongDataText = findViewById(R.id.tvWrongData)
        hint = findViewById(R.id.tvAccountLink)

        hint.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            this.startActivity(intent)
        }


        signInButton.setOnClickListener {
            signInViewModel.signIn(username.text.toString(), password.text.toString()). observe(this,
                {result ->
                    result.doIfSuccess {
                        progressBar.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                    }
                    result.doIfFailure { error, data ->
                        progressBar.visibility = View.GONE
                        wrongDataText.text = error

                    }
                    result.doIfLoading {
                        if(it){
                            progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            )
        }
    }
}