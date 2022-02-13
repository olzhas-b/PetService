/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

data class Pet(
    val name: String,
    val type: Int,
    val breed: String,
    val weight: Int,
    val image: String,
)

data class PetResponse(
    val pets: List<Pet>
)
