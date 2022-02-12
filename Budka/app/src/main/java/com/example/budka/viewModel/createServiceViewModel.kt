/*
 * *
 *  * Created by Ali Ashkeyev on 05.02.2022, 15:11
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 05.02.2022, 15:11
 *
 */

package com.example.budka.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budka.data.model.CountryData

class createServiceViewModel: ViewModel() {
    val selectedPets = MutableLiveData<String>()

}