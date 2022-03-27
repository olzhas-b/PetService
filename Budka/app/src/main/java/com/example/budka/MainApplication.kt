/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.budka.R
import com.example.budka.di.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            koin.loadModules(listOf(viewModelModule, useCaseModule, repositoryModule, networkModule,
            SignInViewModeModule, SignInUseCaseModule, SignInRepositoryModule, sessionManagerModule))
            koin.createRootScope()
        }
    }
}