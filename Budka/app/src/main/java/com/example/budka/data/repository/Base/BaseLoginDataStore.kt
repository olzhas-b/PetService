/*
 * *
 *  * Created by Ali Ashkeyev on 15.01.22 13:31
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 15.01.22 13:31
 *
 */

package com.example.budka.data.repository.Base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.LoginData
import com.example.budka.data.model.LoginResponse
import com.example.budka.data.model.SessionManager
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BaseLoginDataStore(@PublishedApi internal val service: ApiService, var context: Context) {
    abstract fun validateWithLogin(data: LoginData): LiveData<LoginResponse>


    inline fun authenticate(crossinline call: (ApiService) -> Deferred<Response<LoginResponse>>): LiveData<LoginResponse> {
        lateinit var sessionManager: SessionManager
        val result = MutableLiveData<LoginResponse>()

        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        sessionManager = SessionManager(context)
                        sessionManager.saveLoginData(response.body()!!)
                        result.value = response.body()

                    } else {
                        result.value?.message = response.body()?.message.toString()
                        Timber.d("Error occurred with code ${response.code()}")
                    }

                } catch (e: HttpException) {
                    result.value?.message = e.message
                    Timber.d("Error: ${e.message()}")
                } catch (e: Throwable) {
                    result.value?.message = e.message
                    Timber.d("Error: ${e.message}")
                }
            }
        }

        return result
    }
}