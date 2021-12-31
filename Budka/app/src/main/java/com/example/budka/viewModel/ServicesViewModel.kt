package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.Services
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.domain.useCase.ServicesUseCase

class ServicesViewModel (private val servicesUseCase: ServicesUseCase): BaseViewModel() {
    private var userServicesList = MutableLiveData<List<Services>>()

    fun fetchUserServicesList(user_id: Int){
        userServicesList = servicesUseCase.getUserServices(user_id) as MutableLiveData<List<Services>>
    }

    fun getUserServicesList(): LiveData<List<Services>> {
        return userServicesList
    }

}