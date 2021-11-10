package com.example.budka.di

import com.example.budka.data.repository.DataStore.PetSittersListDataStore
import com.example.budka.data.repository.DataStore.PetsListDataStore
import com.example.budka.domain.useCase.PetSittersListUseCase
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.viewModel.PetSittersListViewModel
import com.example.budka.viewModel.PetsListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ PetsListViewModel(get())}
    viewModel {  PetSittersListViewModel(get()) }
}

val useCaseModule = module {
    single{PetsListUseCase(get<PetsListDataStore>())}
    single { PetSittersListUseCase(get<PetSittersListDataStore>()) }
}

val repositoryModule = module{
    single{PetsListDataStore()}
    single{PetSittersListDataStore()}
}