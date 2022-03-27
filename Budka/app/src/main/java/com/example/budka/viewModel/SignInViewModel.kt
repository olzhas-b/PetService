/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:43
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:43
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.budka.data.model.*
import com.example.budka.domain.useCase.LoginUseCase

open class SignInViewModel(val loginUseCase: LoginUseCase): ViewModel() {



    fun signIn(login: String, password: String): LiveData<NetworkResult<LoginResponse>> {
        return loginUseCase.Login(LoginData(SignInModel(login, password)))

    }

    fun register(signUpData: SignUpData): LiveData<NetworkResult<ServiceProvider>> {
        return loginUseCase.Register(signUpData)

    }

}