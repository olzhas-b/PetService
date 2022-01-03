/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model


data class ServiceDetail(
    val service_id: Int,
    val description: String?,
    val acceptablePets: List<String>?,
    val acceptableSize: List<String>?,
    val additionalProperties: List<Properties>?,
//    val user: PetSitterDetail?
)

data class Properties(
    val label: String?,
    val text: String?
)