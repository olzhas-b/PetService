package com.example.budka.data.repository.DataStore

import androidx.lifecycle.LiveData
import com.example.budka.data.api.ApiService
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceDetail
import com.example.budka.data.repository.Base.BasePetSitterDetailDataStore
import com.example.budka.domain.repository.ServiceDetailRepository

class ServiceDetailDataStore(apiService: ApiService): ServiceDetailRepository, BasePetSitterDetailDataStore(apiService) {

    override fun getServiceDetail(serviceId: Int): LiveData<ServiceDetail> {
        return fetchData { service.getServiceDetail(serviceId)}
    }


}