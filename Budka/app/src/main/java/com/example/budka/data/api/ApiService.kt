package com.example.budka.data.api

import com.example.budka.data.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("pets")
    fun getPets(): Deferred<Response<PetResponse>>

    @GET("pets")
    fun getUserPets(@Query("userId") userId: Int): Deferred<Response<PetResponse>>

    @GET("serviceProviders")
    fun getPetSitters(@Query("serviceType") serviceType: String): Deferred<Response<ServiceProvidersList>>

    @GET("serviceProviders/{serviceId}")
    fun getServiceDetail(@Path("serviceId") serviceId: Int): Deferred<Response<ServiceDetail>>

    @GET("services")
    fun getUserServices(@Query("userId") userId: Int): Deferred<Response<ServiceResponse>>

    @GET
    fun getCountries(@Url url: String): Deferred<Response<Countries>>

}