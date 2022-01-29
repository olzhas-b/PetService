/*
 * *
 *  * Created by Ali Ashkeyev on 14.01.22 20:44
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 14.01.22 20:44
 *
 */

package com.example.budka.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceRequiredField(
    val serviceType: Int,
    val summary: String,
    val petTypes: String,
    val petSize: String,
    val country: String,
    val city: String

): Parcelable
