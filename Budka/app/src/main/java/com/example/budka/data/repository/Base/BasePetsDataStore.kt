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

abstract class BasePetsDataStore(@PublishedApi internal val service: ApiService) {
    abstract fun getAllPets(): LiveData<List<Pet>>
    abstract fun getUserPets(user_id: Int): LiveData<List<Pet>>

    inline fun fetchData(crossinline call: (ApiService) -> Deferred<Response<PetResponse>>): LiveData<List<Pet>> {
        val result = MutableLiveData<List<Pet>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = response.body()?.pets
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