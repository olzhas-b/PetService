/*
 * *
 *  * Created by Ali Ashkeyev on 13.02.2022, 0:08
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.02.2022, 0:08
 *
 */

package com.example.budka.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LongLat(
    val longitude: Double,
    val latitude: Double

): Parcelable
