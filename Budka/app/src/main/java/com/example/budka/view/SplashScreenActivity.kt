/*
 * *
 *  * Created by Ali Ashkeyev on 13.06.2022, 21:01
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.06.2022, 21:01
 *
 */

package com.example.budka.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.budka.databinding.ActivityRegistrationBinding
import com.example.budka.viewModel.SignInViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}