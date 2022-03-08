/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ServicesRepository {
    fun getUserServices(user_id:Int): LiveData<List<ServiceProvider>>
    fun createService(images: List<MultipartBody.Part>,
                      body: CreateServiceModel
    ): LiveData<CreateServiceModel>

    fun updateService(images: List<MultipartBody.Part>,
                      body: CreateServiceModel,
                      serviceId: Int
    ): LiveData<CreateServiceModel>
}