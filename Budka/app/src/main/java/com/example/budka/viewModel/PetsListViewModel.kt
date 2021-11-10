package com.example.budka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.Pet
import com.example.budka.domain.useCase.PetsListUseCase

class PetsListViewModel(private val petsListUseCase: PetsListUseCase): BaseViewModel() {
    private var petsList = MutableLiveData<List<Pet>>()
    init {
        fetchPetsList()
    }
    fun fetchPetsList(){
        petsList = petsListUseCase.getPetsList() as MutableLiveData<List<Pet>>
    }

    fun getPetsList(): LiveData<List<Pet>>{
        return petsList
    }
}