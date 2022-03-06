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

@Parcelize
data class ServiceDetail(
    val service_id: Int,
    val description: String?,
    val acceptablePets: String,
    val acceptableSize: Int,
    val additionalProperties: List<Properties>?,
    val longitude: Double,
    val latitude: Double,
//    val user: PetSitterDetail?
): Parcelable

@Parcelize
data class Properties(
    val label: String?,
    val text: String?
): Parcelable

data class CreateServiceModel(
    val serviceType: Int,
    val price: Int,
    val currencyCode: String,
    val pricePerTime: String,
    val longitude: Double,
    val latitude: Double,
    val description: String?,
    val acceptablePets: String?,
    val acceptableSize: Int?,
    val additionalProperties: List<Properties>?,
)


