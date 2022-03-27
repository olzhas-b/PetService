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
import com.example.budka.R
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BasePetSitterDetailDataStore(@PublishedApi internal val service: ApiService) {
    abstract fun getServiceDetail(serviceId: Int):  LiveData<NetworkResult<ServiceDetail>>
    inline fun fetchData(crossinline call: (ApiService) -> Deferred<Response<ServiceDetail>>):  LiveData<NetworkResult<ServiceDetail>> {
        val result = MutableLiveData<NetworkResult<ServiceDetail>>()
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