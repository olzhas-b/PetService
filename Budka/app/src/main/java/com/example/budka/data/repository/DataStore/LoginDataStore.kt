/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:36
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:36
 *
 */

package com.example.budka.data.repository.DataStore

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.LoginData
import com.example.budka.data.model.LoginResponse
import com.example.budka.data.repository.Base.BaseLoginDataStore
import com.example.budka.domain.repository.LoginRepository

class LoginDataStore (apiService: ApiService, context: Context): LoginRepository, BaseLoginDataStore(apiService, context) {
    override fun validateWithLogin(data: LoginData): LiveData<LoginResponse> {
        return authenticate {service.validateWithLogin("http://192.168.43.226:8081/api/v1/user/sign-in", data.signIn) }
    }
}