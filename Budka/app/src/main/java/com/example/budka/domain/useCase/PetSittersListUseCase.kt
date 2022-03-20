/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.*
import com.example.budka.domain.repository.PetSittersListRepository
import com.example.budka.domain.repository.PetsListRepository

class PetSittersListUseCase (val petSittersListRepository: PetSittersListRepository){
    fun getPetSittersList(serviceType: Int, country: String?, city: String?, petType: String?):  LiveData<NetworkResult<List<ServiceProvider>>> {
        return petSittersListRepository.getPetSitters(serviceType, country, city, petType)
    }

    fun putLike(serviceId: Int): LiveData<NetworkResult<String>>{
        return petSittersListRepository.putLike(serviceId)
    }

    fun deleteLike(serviceId: Int): LiveData<NetworkResult<String>>{
        return petSittersListRepository.deleteLike(serviceId)
    }


    fun getFavoriteServices():  LiveData<NetworkResult<List<ServiceProvider>>> {
        return petSittersListRepository.getFavoriteServices()
    }
}