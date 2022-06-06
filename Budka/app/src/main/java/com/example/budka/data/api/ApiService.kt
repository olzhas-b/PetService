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
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

const val API = "api/v1/"

interface ApiService {

    @GET(API + "profile")
    fun getProfileByToken(): Deferred<Response<User>>

    @GET(API + "profile/{userId}")
    fun getUserProfile(@Path("userId") userId: Int): Deferred<Response<User>>

    @POST(API + "user/verify")
    fun verifyPhone(): Deferred<Response<String>>

    @GET(API + "user/pet/all")
    fun getPets(): Deferred<Response<List<Pet>>>

    @GET(API + "user/pet/{petId}")
    fun getPetDetail(@Path("petId") petId: Int): Deferred<Response<Pet>>

    @Multipart
    @POST(API + "user/pet/{petId}/attachment")
    fun uploadAttachment(@Path("petId") petId: Int,
                         @Part attachments : List<MultipartBody.Part>): Deferred<Response<String>>

    @DELETE(API +"user/pet/attachment/{petId}")
    fun deletePetDoc(@Path("petId") petId: Int): Deferred<Response<String>>

    @GET(API +"user/{userId}/pet")
    fun getUserPets(@Path("userId") userId: Int): Deferred<Response<List<Pet>>>

    @POST(API +"user/estimate/{userId}")
    fun setRating(@Path("userId") userId: Int,
                    @Query("score") score: Int): Deferred<Response<String>>

    @Multipart
    @PUT(API +"profile/edit")
    @JvmSuppressWildcards
    fun updateProfile(
        @Part image :MultipartBody.Part,
        @Part("body")  body: UserUpdate,
    ): Deferred<Response<User>>

    @Multipart
    @POST(API +"user/pet/new")
    @JvmSuppressWildcards
    fun createPet(
        @Part image :MultipartBody.Part,
        @Part("body")  body: PetCreate,
    ): Deferred<Response<Pet>>

    @Multipart
    @POST(API + "user/pet/{petId}")
    @JvmSuppressWildcards
    fun updatePet(
        @Part image :MultipartBody.Part,
        @Part("body")  body: PetCreate,
        @Path("petId") serviceId: Int
    ): Deferred<Response<Pet>>

    @DELETE(API +"user/pet/{petId}/delete")
    fun deletePet(@Path("petId") petId: Int): Deferred<Response<String>>

    @DELETE(API +"service/{serviceId}")
    fun deleteService(@Path("serviceId") petId: Int): Deferred<Response<String>>

    @GET(API +"service")
    fun getPetSitters(@QueryMap(encoded = true) country: Map<String, String?>): Deferred<Response<ServiceProviderResponse>>

    @Multipart
    @POST(API +"service/new")
    @JvmSuppressWildcards
    fun createService(
        @Part images :List<MultipartBody.Part>,
        @Part("body") body: CreateServiceModel,
    ): Deferred<Response<String>>


    @Multipart
    @POST(API + "service/{serviceId}")
    @JvmSuppressWildcards
    fun updateService(
        @Part images :List<MultipartBody.Part>,
        @Part("body") body: CreateServiceModel,
        @Path("serviceId") serviceId: Int
    ): Deferred<Response<String>>

    @GET(API +"service/{serviceId}/detail")
    fun getServiceDetail(@Path("serviceId") serviceId: Int): Deferred<Response<ServiceDetail>>

    @GET(API +"service")
    fun getUserServices(@Query("userId") userId: Int): Deferred<Response<ServiceProviderResponse>>

    @GET(API + "countries")
    fun getCountries(): Deferred<Response<List<CountryData>>>

    @POST(API +"service/favorite/{serviceId}/add")
    fun putLike(@Path("serviceId") serviceId: Int): Deferred<Response<String>>

    @DELETE(API +"service/favorite/{serviceId}/remove")
    fun deleteLike(@Path("serviceId") serviceId: Int): Deferred<Response<String>>

    @GET(API +"service/favorite")
    fun getFavoriteServices(): Deferred<Response<List<ServiceProvider>>>

    @POST
    fun validateWithLogin(
        @Url url: String,
        @Body data: SignInModel
    ):Deferred<Response<LoginResponse>>

    @POST
    fun deleteSession(
        @Url url: String,
    ):Deferred<Response<String>>

    @POST
    fun register(
        @Url url: String,
        @Body data: SignUpData
    ):Deferred<Response<ServiceProvider>>

}