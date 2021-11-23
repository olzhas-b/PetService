package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceDetail
import com.example.budka.domain.repository.PetsListRepository
import com.example.budka.domain.repository.ServiceDetailRepository

class ServiceDetailUseCase (val serviceDetailRepository: ServiceDetailRepository){
    fun getServiceDetail(serviceId: Int): LiveData<ServiceDetail> {
        return serviceDetailRepository.getServiceDetail(serviceId)
    }
}