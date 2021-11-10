package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.User
import com.example.budka.domain.useCase.PetSittersListUseCase
import com.example.budka.domain.useCase.PetsListUseCase

class PetSittersListViewModel (private val petSittersListUseCase: PetSittersListUseCase): BaseViewModel() {
    private var petSittersList = MutableLiveData<List<User>>()

    init {
        fetchPetSittersList()
    }

    fun fetchPetSittersList() {
        petSittersList = petSittersListUseCase.getPetSittersList() as MutableLiveData<List<User>>
    }

    fun getPetSittersList(): LiveData<List<User>> {
        return petSittersList
    }
}