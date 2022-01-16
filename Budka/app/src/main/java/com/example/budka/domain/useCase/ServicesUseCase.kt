/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.ServiceDetail
import com.example.budka.data.model.Services
import com.example.budka.domain.repository.ServiceDetailRepository
import com.example.budka.domain.repository.ServicesRepository

class ServicesUseCase (private val servicesRepository: ServicesRepository){
    fun getUserServices(user_id: Int): LiveData<List<Services>> {
        return servicesRepository.getUserServices(user_id)
    }
}