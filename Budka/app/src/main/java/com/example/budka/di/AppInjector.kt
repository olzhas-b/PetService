/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.di

import com.example.budka.data.api.ApiClient
import com.example.budka.data.model.SessionManager
import com.example.budka.data.repository.DataStore.*
import com.example.budka.domain.useCase.*
import com.example.budka.viewModel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ PetsListViewModel(get())}
    viewModel {  PetSittersListViewModel(get()) }
    viewModel {ServiceDetailViewModel(get())}
    viewModel {ServicesViewModel(get())}
    viewModel { CountriesListViewModel(get())}
    viewModel { createServiceViewModel() }
    viewModel {ProfileViewModel(get())}
}

val SignInViewModeModule = module{
    viewModel { SignInViewModel(get()) }
}

val useCaseModule = module {
    single{PetsListUseCase(get<PetsListDataStore>())}
    single { PetSittersListUseCase(get<PetSittersListDataStore>()) }
    single { ServiceDetailUseCase(get<ServiceDetailDataStore>()) }
    single { ServicesUseCase(get<ServicesDataStore>()) }
    single { CountryListUseCase(get<CountriesDataStore>()) }
    single { ProfileUseCase(get<MyPageDataStore>())}
}

val SignInUseCaseModule = module{
    single { LoginUseCase(get<LoginDataStore>()) }
}

val repositoryModule = module{
    single{PetsListDataStore(get())}
    single{PetSittersListDataStore(get(), androidApplication())}
    single{ServiceDetailDataStore(get())}
    single{ServicesDataStore(get())}
    single { CountriesDataStore(get()) }
    single { MyPageDataStore(get())}
}

val SignInRepositoryModule = module {
    single { LoginDataStore(get(), androidApplication()) }
}

val networkModule = module {
    single { ApiClient.create(okHttpClient = get())}
    single { ApiClient.getOkHttpClient(authInterceptor = get())}
    single { ApiClient.getAuthInterceptor(sessionManager = get<SessionManager>())}

}

val sessionManagerModule = module {
    single { SessionManager(androidApplication()) }
}
