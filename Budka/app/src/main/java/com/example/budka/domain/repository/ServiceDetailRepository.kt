package com.example.budka.domain.repository

import androidx.lifecycle.LiveData
import com.example.budka.data.model.ServiceDetail
import com.example.budka.data.model.ServiceProvider

interface ServiceDetailRepository {
    fun getServiceDetail(serviceId: Int): LiveData<ServiceDetail>
}