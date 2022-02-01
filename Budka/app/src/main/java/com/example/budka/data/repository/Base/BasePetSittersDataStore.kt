/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.repository.Base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BasePetSittersDataStore(@PublishedApi internal val service: ApiService) {
    abstract fun getPetSitters(serviceType: String): LiveData<List<ServiceProvider>>

    inline fun fetchData(crossinline call: (ApiService) -> Deferred<Response<List<ServiceProvider>>>): LiveData<List<ServiceProvider>> {
        val result = MutableLiveData<List<ServiceProvider>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = response.body()
                    } else {
                        Timber.d("Error occurred with code ${response.code()}")
                    }
                } catch (e: HttpException){
                    Timber.d("Error: ${e.message()}")
                } catch (e: Throwable){
                    Timber.d("Error: ${e.message}")
                }
            }
        }
        return result

    }
}