/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:38
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:38
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.*
import com.example.budka.domain.repository.LoginRepository

class LoginUseCase(val accountRepository: LoginRepository) {
    fun Login(data: LoginData):  LiveData<NetworkResult<LoginResponse>> {
        return accountRepository.validateWithLogin(data)
    }

    fun Register(data: SignUpData):  LiveData<NetworkResult<ServiceProvider>> {
        return accountRepository.registerUser(data)
    }
}