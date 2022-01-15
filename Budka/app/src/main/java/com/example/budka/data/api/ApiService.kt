/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.api

import com.example.budka.data.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

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

    @POST("user/sign-in")
    fun validateWithLogin(
        @Body data: SignInModel
    ):Deferred<Response<LoginResponse>>

}