/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.*
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.domain.useCase.ServicesUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServicesViewModel (private val servicesUseCase: ServicesUseCase): BaseViewModel() {
    private var userServicesList = MutableLiveData<List<ServiceProvider>>()

    fun fetchUserServicesList(user_id: Int){
        userServicesList = servicesUseCase.getUserServices(user_id) as MutableLiveData<List<ServiceProvider>>
    }

    fun getUserServicesList(): LiveData<List<ServiceProvider>> {
        return userServicesList
    }

    fun createService(images: List<MultipartBody.Part>,
                      body: CreateServiceModel
    ): LiveData<CreateServiceModel>{
        return servicesUseCase.createService(images, body)
    }

}