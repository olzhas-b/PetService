package com.example.budka.di

import com.example.budka.data.api.ApiClient
import com.example.budka.data.repository.DataStore.PetSittersListDataStore
import com.example.budka.data.repository.DataStore.PetsListDataStore
import com.example.budka.data.repository.DataStore.ServiceDetailDataStore
import com.example.budka.domain.useCase.PetSittersListUseCase
import com.example.budka.domain.useCase.PetsListUseCase
import com.example.budka.domain.useCase.ServiceDetailUseCase
import com.example.budka.viewModel.PetSittersListViewModel
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ServiceDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ PetsListViewModel(get())}
    viewModel {  PetSittersListViewModel(get()) }
    viewModel {ServiceDetailViewModel(get())}
}

val useCaseModule = module {
    single{PetsListUseCase(get<PetsListDataStore>())}
    single { PetSittersListUseCase(get<PetSittersListDataStore>()) }
    single { ServiceDetailUseCase(get<ServiceDetailDataStore>()) }
}

val repositoryModule = module{
    single{PetsListDataStore(get())}
    single{PetSittersListDataStore(get())}
    single{ServiceDetailDataStore(get())}
}

val networkModule = module {
    single { ApiClient.create(okHttpClient = get())}
    single { ApiClient.getOkHttpClient(authInterceptor = get())}
    single { ApiClient.getAuthInterceptor()}
}