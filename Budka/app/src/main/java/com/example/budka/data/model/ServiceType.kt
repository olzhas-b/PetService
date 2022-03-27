/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

package com.example.budka.data.model

enum class ServiceType (val value: String) {
    PETSITTING("Зооняни"),
    PETWALKING("Выгул"),
    VET("Ветеринары"),
    TRAINING("Дрессировка"),
    GROOMING("Груминг"),
    ZOOHOSTEL("Зоогостиницы");
    companion object{
        fun from(s: String): ServiceType? = values().find { it.value == s }
    }

}