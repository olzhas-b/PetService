/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

import android.os.Parcelable
import androidx.room.*
import com.example.budka.data.localDB.Converters
import com.example.budka.utils.Constants
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

data class ServiceProviderResponse(
    @SerializedName("rows")
    val rows: List<ServiceProvider>,
    @SerializedName("total")
    val total: Int
)

@Entity(tableName = Constants.SERVICE_PROVIDERS_TABLE)
@Parcelize
data class ServiceProvider(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "service_type")
    @SerializedName("serviceType")
    val serviceType: Int?,
    @SerializedName("price")
    val price: Int?,
    @ColumnInfo(name = "currency_code")
    @SerializedName("currencyCode")
    val currencyCode: String?,
    @ColumnInfo(name = "price_per_time")
    @SerializedName("pricePerTime")
    val pricePerTime: String?,
    @SerializedName("status")
    val status: Int?,
    @Embedded(prefix = "user")
    @SerializedName("user")
    val user: @RawValue
    User?,
    @SerializedName("images")
    val images: List<String>?,
    @ColumnInfo(name = "is_favorite")
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
    val countRating: Int?,
    @SerializedName("cntFavorite")
    val cntFavorite: Int?,
    @SerializedName("averageRating")
    val averageRating: Float,
    @SerializedName("image")
    val avatar: String?,
    @SerializedName("isVerified")
    val isVerified: Boolean = false
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

