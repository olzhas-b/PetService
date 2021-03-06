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
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BasePetsDataStore(@PublishedApi internal val service: ApiService) {
    abstract fun getAllPets():  LiveData<NetworkResult<List<Pet>>>
    abstract fun getUserPets(user_id: Int): LiveData<NetworkResult<List<Pet>>>
    abstract fun getPetDetail(petId: Int): LiveData<NetworkResult<Pet>>
    abstract fun createPet(image: MultipartBody.Part,
                        body: PetCreate): LiveData<NetworkResult<Pet>>
    abstract fun updatePet(image: MultipartBody.Part,
                           body: PetCreate, petId: Int ): LiveData<NetworkResult<Pet>>
    abstract fun deletePet(petId: Int): LiveData<NetworkResult<String>>
    abstract fun deletePetDoc(petId: Int): LiveData<NetworkResult<String>>
    abstract fun uploadAttachment(petId: Int, attachments: List<MultipartBody.Part>): LiveData<NetworkResult<String>>

    inline fun fetchData(crossinline call:
                             (ApiService) -> Deferred<Response<List<Pet>>>)
    : LiveData<NetworkResult<List<Pet>>> {
        val result = MutableLiveData<NetworkResult<List<Pet>>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body())
                    } else {
                        result.value = NetworkResult
                            .Error("???????????? ???????????????????? ?? ??????????${response.code()}")
                    }
                } catch (e: HttpException){
                    result.value = NetworkResult.Error("????????????: ${e.message()}")
                } catch (e: Throwable){
                    result.value = NetworkResult.Error("????????????: ${e.message}")
                }
            }
        }
        return result
    }

    inline fun getPetResponse(crossinline call: (ApiService) -> Deferred<Response<Pet>>)
    : LiveData<NetworkResult<Pet>> {
        val result = MutableLiveData<NetworkResult<Pet>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body())
                    } else {
                        result.value = NetworkResult
                            .Error("???????????? ???????????????????? ?? ??????????${response.code()}")
                    }
                } catch (e: HttpException){
                    result.value = NetworkResult.Error("????????????: ${e.message()}")
                } catch (e: Throwable){
                    result.value = NetworkResult.Error("????????????: ${e.message}")
                }
            }
        }
        return result
    }

    inline fun deleteResponse(crossinline call: (ApiService) -> Deferred<Response<String>>):LiveData<NetworkResult<String>> {
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
                        result.value = NetworkResult.Error("???????????? ???????????????????? ?? ??????????${response.code()}")
                        Timber.d("Error occurred with code ${response.code()}")
                    }
                } catch (e: HttpException){
                    result.value = NetworkResult.Error("????????????: ${e.message()}")
                    Timber.d("Error: ${e.message()}")
                } catch (e: Throwable){
                    result.value = NetworkResult.Error("????????????: ${e.message}")
                    Timber.d("Error: ${e.message}")
                }
            }
        }
        return result

    }
}