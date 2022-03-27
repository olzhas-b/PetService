/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:38
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:38
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.*

interface LoginRepository {
    fun validateWithLogin(data: LoginData):  LiveData<NetworkResult<LoginResponse>>
    fun registerUser(data: SignUpData):  LiveData<NetworkResult<ServiceProvider>>
}