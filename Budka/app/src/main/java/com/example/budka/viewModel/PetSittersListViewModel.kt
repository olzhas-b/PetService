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
import com.example.budka.data.model.*
import com.example.budka.domain.useCase.PetSittersListUseCase
import com.example.budka.domain.useCase.PetsListUseCase

class PetSittersListViewModel (private val petSittersListUseCase: PetSittersListUseCase): BaseViewModel() {
    var petSittersList = MutableLiveData< NetworkResult<List<ServiceProvider>>>()
    private var favoriteServicesList = MutableLiveData<NetworkResult<List<ServiceProvider>>>()


    fun fetchPetSittersList(serviceType: Int, country: String?, city: String?, petType: String?) {
        petSittersList = petSittersListUseCase.getPetSittersList(serviceType, country, city, petType) as MutableLiveData<NetworkResult<List<ServiceProvider>>>
    }

    fun getPetSittersList(): LiveData<NetworkResult<List<ServiceProvider>>> {
        return petSittersList
    }

    fun fetchFavoriteServices() {
        favoriteServicesList = petSittersListUseCase.getFavoriteServices() as MutableLiveData<NetworkResult<List<ServiceProvider>>>
    }

    fun getFavoriteServices(): LiveData<NetworkResult<List<ServiceProvider>>> {
        return favoriteServicesList
    }

    fun putLike(serviceId: Int): LiveData<NetworkResult<String>> {
        return petSittersListUseCase.putLike(serviceId)
    }

    fun deleteLike(serviceId: Int): LiveData<NetworkResult<String>> {
       return petSittersListUseCase.deleteLike(serviceId)
    }
}