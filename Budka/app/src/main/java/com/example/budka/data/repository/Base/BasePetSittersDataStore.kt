/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.repository.Base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.localDB.ServiceProvidersDao
import com.example.budka.data.localDB.ServiceProvidersDatabase
import com.example.budka.data.model.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BasePetSittersDataStore(@PublishedApi internal val service: ApiService, var context: Context) {
    abstract fun getPetSitters(serviceType: Int, country: String?, city: String?, petType: String?): LiveData<NetworkResult<ServiceProviderResponse>>
    abstract fun putLike(serviceId: Int): LiveData<NetworkResult<String>>
    abstract fun deleteLike(serviceId: Int): LiveData<NetworkResult<String>>
    abstract fun getFavoriteServices(): LiveData<NetworkResult<List<ServiceProvider>>>
    var dao: ServiceProvidersDao = ServiceProvidersDatabase.getDatabase(context).serviceProviderDao()

    inline fun fetchData(crossinline call:
                             (ApiService) -> Deferred<Response<ServiceProviderResponse>>):
            LiveData<NetworkResult<ServiceProviderResponse>> {
        val result = MutableLiveData<NetworkResult<ServiceProviderResponse>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = NetworkResult.Success(response.body())
                        result.value?.let { it.data?.let { it1 -> dao.insertAll(it1.rows) } }
                    } else {
                        val data = withContext(Dispatchers.IO) {
                            dao.getAll()
                        }
                        if (data.isNotEmpty()) {
                            result.value = NetworkResult.Success(ServiceProviderResponse(data, data.size))
                        }
                    }
                } catch (e: HttpException){
                    val data = withContext(Dispatchers.IO) {
                        dao.getAll()
                    }
                    if (data.isNotEmpty()) {
                        result.value = NetworkResult.Success(ServiceProviderResponse(data, data.size))
                    }                }
                catch (e: Throwable){
                    val data = withContext(Dispatchers.IO) {
                        dao.getAll()
                    }
                    if (data.isNotEmpty()) {
                        result.value = NetworkResult.Success(ServiceProviderResponse(data, data.size))
                    }                }
            }
        }
        return result
    }

    inline fun fetchDataFav(crossinline call: (ApiService) -> Deferred<Response<List<ServiceProvider>>>): LiveData<NetworkResult<List<ServiceProvider>>> {
        val result = MutableLiveData<NetworkResult<List<ServiceProvider>>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){
                try {
                    result.value = NetworkResult.Loading()
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value =  NetworkResult.Success(response.body())
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

    inline fun favResponse(crossinline call: (ApiService) -> Deferred<Response<String>>): LiveData<NetworkResult<String>> {
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