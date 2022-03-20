/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceDetail
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.domain.useCase.ServiceDetailUseCase

class ServiceDetailViewModel(private val serviceDetailUseCase: ServiceDetailUseCase): BaseViewModel() {
    private var serviceDetail = MutableLiveData< NetworkResult<ServiceDetail>>()
    fun fetchServiceDetail(serviceId: Int){
        serviceDetail = serviceDetailUseCase.getServiceDetail(serviceId) as MutableLiveData<NetworkResult<ServiceDetail>>
    }

    fun getServiceDetail(): LiveData<NetworkResult<ServiceDetail>> {
        return serviceDetail
    }
}