/*
 * *
 *  * Created by Ali Ashkeyev on 21.02.2022, 10:38
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21.02.2022, 10:38
 *
 */

package com.example.budka.data.repository.Base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.data.model.UserUpdate
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BaseProfileDataStore (@PublishedApi internal val service: ApiService) {
    abstract fun getProfileByToken(): LiveData<NetworkResult<User>>
    abstract fun updateProfile(image: MultipartBody.Part,
                               body: UserUpdate): LiveData<NetworkResult<User>>
    abstract fun getUserProfile(userId: Int) : LiveData<NetworkResult<User>>
    abstract fun setRating(userId: Int, rating: Int): LiveData<NetworkResult<String>>
    abstract fun deleteSession(): LiveData<NetworkResult<String>>
    abstract fun verifyPhone(): LiveData<NetworkResult<String>>


    inline fun profileByTokenResponse(crossinline call: (ApiService) -> Deferred<Response<User>>): LiveData<NetworkResult<User>> {
        val result = MutableLiveData<NetworkResult<User>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body())
                    }else {
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

    inline fun deleteResponse(crossinline call: (ApiService) -> Deferred<Response<String>>): LiveData<NetworkResult<String>> {
        val result = MutableLiveData<NetworkResult<String>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body())
                    } else {
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