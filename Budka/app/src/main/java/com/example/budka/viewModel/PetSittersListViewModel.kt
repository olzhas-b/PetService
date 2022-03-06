/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.domain.useCase.PetSittersListUseCase
import com.example.budka.domain.useCase.PetsListUseCase

class PetSittersListViewModel (private val petSittersListUseCase: PetSittersListUseCase): BaseViewModel() {
    var petSittersList = MutableLiveData<List<ServiceProvider>>()
    private var favoriteServicesList = MutableLiveData<List<ServiceProvider>>()


    fun fetchPetSittersList(serviceType: Int) {
        petSittersList = petSittersListUseCase.getPetSittersList(serviceType) as MutableLiveData<List<ServiceProvider>>
    }

    fun getPetSittersList(): LiveData<List<ServiceProvider>> {
        return petSittersList
    }

    fun fetchFavoriteServices() {
        favoriteServicesList = petSittersListUseCase.getFavoriteServices() as MutableLiveData<List<ServiceProvider>>
    }

    fun getFavoriteServices(): LiveData<List<ServiceProvider>> {
        return favoriteServicesList
    }

    fun putLike(serviceId: Int) {
        petSittersListUseCase.putLike(serviceId)
    }

    fun deleteLike(serviceId: Int) {
        petSittersListUseCase.deleteLike(serviceId)
    }
}