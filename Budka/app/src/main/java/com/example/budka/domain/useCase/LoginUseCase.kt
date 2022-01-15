/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:38
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:38
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.LoginData
import com.example.budka.data.model.LoginResponse
import com.example.budka.domain.repository.LoginRepository

class LoginUseCase(val accountRepository: LoginRepository) {
    fun Login(data: LoginData): LiveData<LoginResponse> {
        return accountRepository.validateWithLogin(data)
    }
}