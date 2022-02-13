/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

import com.google.gson.annotations.SerializedName

data class Pet(
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
)

data class PetCreate(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("breed")
    val breed: String,
    @SerializedName("weight")
    val weight: Int?
)

data class PetResponse(
    val pets: List<Pet>
)
