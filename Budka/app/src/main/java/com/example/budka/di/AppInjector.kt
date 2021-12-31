package com.example.budka.di

import com.example.budka.data.api.ApiClient
import com.example.budka.data.repository.DataStore.*
import com.example.budka.domain.useCase.*
import com.example.budka.viewModel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ PetsListViewModel(get())}
    viewModel {  PetSittersListViewModel(get()) }
    viewModel {ServiceDetailViewModel(get())}
    viewModel {ServicesViewModel(get())}
    viewModel { CountriesListViewModel(get())}
}

val useCaseModule = module {
    single{PetsListUseCase(get<PetsListDataStore>())}
    single { PetSittersListUseCase(get<PetSittersListDataStore>()) }
    single { ServiceDetailUseCase(get<ServiceDetailDataStore>()) }
    single { ServicesUseCase(get<ServicesDataStore>()) }
    single { CountryListUseCase(get<CountriesDataStore>()) }
}

val repositoryModule = module{
    single{PetsListDataStore(get())}
    single{PetSittersListDataStore(get())}
    single{ServiceDetailDataStore(get())}
    single{ServicesDataStore(get())}
    single { CountriesDataStore(get()) }
}

val networkModule = module {
    single { ApiClient.create(okHttpClient = get())}
    single { ApiClient.getOkHttpClient(authInterceptor = get())}
    single { ApiClient.getAuthInterceptor()}
}
