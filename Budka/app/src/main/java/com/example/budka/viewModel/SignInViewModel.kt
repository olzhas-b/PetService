/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:43
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:43
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.budka.data.model.LoginData
import com.example.budka.data.model.LoginResponse
import com.example.budka.data.model.SignInModel
import com.example.budka.data.model.Statesealed
import com.example.budka.domain.useCase.LoginUseCase

open class SignInViewModel(val loginUseCase: LoginUseCase): ViewModel() {

    var state = MutableLiveData<Statesealed>()
    var message = MutableLiveData<String>()

    fun signIn(login: String, password: String): LiveData<LoginResponse> {
        state.value = Statesealed.ShowLoading
        val result = loginUseCase.Login(LoginData(SignInModel(login, password)))

        val ss = Observer<LoginResponse> { loginResponse ->
            if (loginResponse.message ==null) {

                state.value = Statesealed.Result
                state.value = Statesealed.HideLoading
            } else {
                state.value = Statesealed.FailedLoading
                state.value = Statesealed.HideLoading
                message.value = loginResponse.message
            }
            result.removeObserver({})
        }

        result.observeForever(ss)
        return result

    }

}