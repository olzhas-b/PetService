package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceDetail
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.domain.useCase.ServiceDetailUseCase

class ServiceDetailViewModel(private val serviceDetailUseCase: ServiceDetailUseCase): BaseViewModel() {
    private var serviceDetail = MutableLiveData<ServiceDetail>()
    fun fetchServiceDetail(serviceId: Int){
        serviceDetail = serviceDetailUseCase.getServiceDetail(serviceId) as MutableLiveData<ServiceDetail>
    }

    fun getServiceDetail(): LiveData<ServiceDetail> {
        return serviceDetail
    }
}