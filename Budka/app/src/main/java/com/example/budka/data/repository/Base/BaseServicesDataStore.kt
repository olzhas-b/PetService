package com.example.budka.data.repository.Base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetResponse
import com.example.budka.data.model.ServiceResponse
import com.example.budka.data.model.Services
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BaseServicesDataStore (@PublishedApi internal val service: ApiService) {
    abstract fun getUserServices(user_id: Int): LiveData<List<Services>>

    inline fun fetchData(crossinline call: (ApiService) -> Deferred<Response<ServiceResponse>>): LiveData<List<Services>> {
        val result = MutableLiveData<List<Services>>()
        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main){

                try {
                    Log.d("look", "wee3a")
                    val response = request.await()
                    if (response.isSuccessful) {
                        Log.d("look", "wewe")
                        result.value = response.body()?.services
                    } else {
                        Log.d("look", "sds")
                        Timber.d("Error occurred with code ${response.code()}")
                    }
                } catch (e: HttpException){
                    Timber.d("Error: ${e.message()}")
                } catch (e: Throwable){
                    Timber.d("Error: ${e.message}")
                }
                catch (e: Exception){
                    Log.d("look", e.toString())

                }
            }
        }
        return result

    }
}