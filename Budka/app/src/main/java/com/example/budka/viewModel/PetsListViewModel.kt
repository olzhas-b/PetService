package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.domain.useCase.PetsListUseCase

class PetsListViewModel(private val petsListUseCase: PetsListUseCase): BaseViewModel() {
    private var petsList = MutableLiveData<List<Pet>>()
    private var userPetsList = MutableLiveData<List<Pet>>()
    init {
        fetchPetsList()
    }
    fun fetchPetsList(){
        petsList = petsListUseCase.getPetsList() as MutableLiveData<List<Pet>>
    }

    fun getPetsList(): LiveData<List<Pet>>{
        return petsList
    }

    fun fetchUserPetsList(user_id: Int){
        userPetsList = petsListUseCase.getUserPets(user_id) as MutableLiveData<List<Pet>>
    }

    fun getUserPetsList(): LiveData<List<Pet>>{
        return userPetsList
    }

}