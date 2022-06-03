/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

import com.google.gson.annotations.SerializedName
import dev.icerock.moko.parcelize.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Pet(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("breed")
    val breed: String,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("attachments")
    val docs: @RawValue List<Attachment>?,
    @SerializedName("userID")
    val userID: Int,
    @SerializedName("expireDate")
    val expireDate: String?
): Parcelable

@Parcelize
data class Attachment(
    @SerializedName("contentType")
    val contentType: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
): Parcelable

data class PetCreate(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("breed")
    val breed: String,
    @SerializedName("weight")
    val weight: Int?,
    @SerializedName("expireDate")
    val expireDate: String?
)

data class PetResponse(
    val pets: List<Pet>
)
