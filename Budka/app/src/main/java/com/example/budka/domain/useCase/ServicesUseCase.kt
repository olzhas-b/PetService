/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.*
import com.example.budka.domain.repository.ServiceDetailRepository
import com.example.budka.domain.repository.ServicesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServicesUseCase (private val servicesRepository: ServicesRepository){
    fun getUserServices(user_id: Int): LiveData<NetworkResult<List<ServiceProvider>>> {
        return servicesRepository.getUserServices(user_id)
    }

    fun createService(images: List<MultipartBody.Part>,
                      body: CreateServiceModel
    ): LiveData<NetworkResult<String>>{
        return servicesRepository.createService(images, body)
    }

    fun updateService(images: List<MultipartBody.Part>,
                      body: CreateServiceModel,
                      serviceId: Int
    ): LiveData<NetworkResult<String>>{
        return servicesRepository.updateService(images, body, serviceId)
    }

    fun deleteService(serviceId: Int): LiveData<NetworkResult<String>>{
        return servicesRepository.deleteService(serviceId)
    }
}