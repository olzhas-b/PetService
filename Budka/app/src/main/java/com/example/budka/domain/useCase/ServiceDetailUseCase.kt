/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceDetail
import com.example.budka.domain.repository.PetsListRepository
import com.example.budka.domain.repository.ServiceDetailRepository

class ServiceDetailUseCase (val serviceDetailRepository: ServiceDetailRepository){
    fun getServiceDetail(serviceId: Int): LiveData<NetworkResult<ServiceDetail>> {
        return serviceDetailRepository.getServiceDetail(serviceId)
    }
}