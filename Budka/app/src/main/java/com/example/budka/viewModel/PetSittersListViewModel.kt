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
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.domain.useCase.PetSittersListUseCase
import com.example.budka.domain.useCase.PetsListUseCase

class PetSittersListViewModel (private val petSittersListUseCase: PetSittersListUseCase): BaseViewModel() {
    private var petSittersList = MutableLiveData<List<ServiceProvider>>()


    fun fetchPetSittersList(serviceType: String) {
        petSittersList = petSittersListUseCase.getPetSittersList(serviceType) as MutableLiveData<List<ServiceProvider>>
    }

    fun getPetSittersList(): LiveData<List<ServiceProvider>> {
        return petSittersList
    }
}