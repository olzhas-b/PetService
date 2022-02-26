/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable


data class PetOwner(
    val id: Int,
    val username: String,
    val first_name: String,
    val last_name: String,
    val city: String,
    val country: String,
    val location: String,
    val avatar: String,
)

data class ServiceProvidersList(
    val users: List<ServiceProvider>
)

@Parcelize

data class ServiceProvider(
    @SerializedName("id")
    val id: Int,
    @SerializedName("serviceType")
    val serviceType: Int?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("currencyCode")
    val currencyCode: String?,
    @SerializedName("pricePerTime")
    val pricePerTime: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("user")
    val user: @RawValue
    User?,
    @SerializedName("images")
    val images: List<String>?,
    @SerializedName("isFavorite")
    var isFavorite: Boolean?
): Parcelable



data class PetSitterDetail(
    val id: Int,
    val username: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val city: String,
    val country: String,
    val location: String,
    val average_rating: Double,
    val avatar: String,
    val services: List<Services>?
)

data class ServiceResponse(
    val services: List<Services>?
)

data class Services(
    val id: Int,
    val serviceType: String?,
    val price: Int,
    val currencyCode: String?,
    val pricePerTime: String?,
)


@Parcelize
data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("location")
    val location: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("countRating")
    val countRating: Int,
    @SerializedName("averageRating")
    val averageRating: Float,
    @SerializedName("image")
    val avatar: String
): Parcelable

@Parcelize
data class UserUpdate(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("description")
    val description: String,
): Parcelable

