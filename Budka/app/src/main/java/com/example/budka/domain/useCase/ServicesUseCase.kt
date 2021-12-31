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