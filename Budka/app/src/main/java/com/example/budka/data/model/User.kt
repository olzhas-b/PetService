/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class User(
    val id: Int,
    val username: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val city: String,
    val country: String,
    val location: String,
    val bio: String,
    val count_rating: Int,
    val average_rating: Double,
    val avatar: String,
    val services: Services?

)

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
    val id: Int,
    val username: String,
    val first_name: String,
    val last_name: String,
    val city: String,
    val country: String,
    val location: String?,
    val avatar: String,
    val average_rating: Double,
    val service_id: Int,
    val serviceType: String,
    val price: Int,
    val currencyCode: String?,
    val pricePerTime: String?
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



