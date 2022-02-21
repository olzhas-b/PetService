/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.domain.useCase

import androidx.lifecycle.LiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.domain.repository.PetSittersListRepository
import com.example.budka.domain.repository.PetsListRepository

class PetSittersListUseCase (val petSittersListRepository: PetSittersListRepository){
    fun getPetSittersList(serviceType: String): LiveData<List<ServiceProvider>> {
        return petSittersListRepository.getPetSitters(serviceType)
    }

    fun putLike(serviceId: Int){
        petSittersListRepository.putLike(serviceId)
    }

    fun deleteLike(serviceId: Int){
        petSittersListRepository.deleteLike(serviceId)
    }

    fun getFavoriteServices(): LiveData<List<ServiceProvider>> {
        return petSittersListRepository.getFavoriteServices()
    }
}