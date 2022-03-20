/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.repository.Base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.*
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BaseServicesDataStore (@PublishedApi internal val service: ApiService) {
    abstract fun getUserServices(user_id: Int): LiveData<NetworkResult<List<ServiceProvider>>>
    abstract fun createService(images :List<MultipartBody.Part>, body: CreateServiceModel): LiveData<NetworkResult<String>>
    abstract fun updateService(images :List<MultipartBody.Part>, body: CreateServiceModel, serviceId: Int): LiveData<NetworkResult<String>>
    abstract fun deleteService(serviceId: Int):  LiveData<NetworkResult<String>>



    inline fun fetchData(crossinline call: (ApiService) -> Deferred<Response<ServiceProviderResponse>>): LiveData<NetworkResult<List<ServiceProvider>>> {
        val result = MutableLiveData<NetworkResult<List<ServiceProvider>>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){

                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body()?.rows)
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

    inline fun postService(crossinline call: (ApiService) -> Deferred<Response<String>>): LiveData<NetworkResult<String>> {
        val result = MutableLiveData<NetworkResult<String>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){

                try {
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