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
import com.example.budka.data.model.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BaseLoginDataStore(@PublishedApi internal val service: ApiService, var context: Context) {
    abstract fun validateWithLogin(data: LoginData): LiveData<NetworkResult<LoginResponse>>
    abstract fun registerUser(data: SignUpData): LiveData<NetworkResult<ServiceProvider>>


    inline fun authenticate(crossinline call: (ApiService) -> Deferred<Response<LoginResponse>>): LiveData<NetworkResult<LoginResponse>> {
        lateinit var sessionManager: SessionManager
        val result = MutableLiveData<NetworkResult<LoginResponse>>()

        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main) {
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        sessionManager = SessionManager(context)
                        sessionManager.saveLoginData(response.body()!!)
                        result.value = NetworkResult.Success(response.body())

                    }  else {
                        result.value = NetworkResult.Error("Запрос завершился с кодом${response.code()}")
                        Timber.d("Error occurred with code ${response.code()}")
                    }
                } catch (e: HttpException){
                    result.value = NetworkResult.Error("Ошибка: ${e.message()}")
                    Timber.d("Error: ${e.message()}")
                } catch (e: Throwable){
                    result.value = NetworkResult.Error("Ошибка: ${e.message}")
                    Timber.d("Error: ${e.message}")
                }
            }
        }

        return result
    }

    inline fun registrationResponse(crossinline call: (ApiService) -> Deferred<Response<ServiceProvider>>): LiveData<NetworkResult<ServiceProvider>> {
        val result = MutableLiveData<NetworkResult<ServiceProvider>>()

        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main) {
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body())

                    }  else {
                        result.value = NetworkResult.Error("Запрос завершился с кодом${response.code()}")
                        Timber.d("Error occurred with code ${response.code()}")
                    }
                } catch (e: HttpException){
                    result.value = NetworkResult.Error("Ошибка: ${e.message()}")
                    Timber.d("Error: ${e.message()}")
                } catch (e: Throwable){
                    result.value = NetworkResult.Error("Ошибка: ${e.message}")
                    Timber.d("Error: ${e.message}")
                }
            }
        }

        return result
    }
}