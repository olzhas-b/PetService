package com.example.budka.data.repository.Base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.*

abstract class BasePetSittersDataStore {

    inline fun fetchNearestPetSitterData(): LiveData<List<User>> {
        val result = MutableLiveData<List<User>>()
        val items: MutableList<User> = ArrayList<User>()
        items.add(
         User(
                    username = "akimbek",
                    first_name = "Акимбек",
                    last_name = "Байтасов",
                    phone = "+77777777722",
                    city = "Алматы",
                    country = "Казахстан",
                    location = "",
                    bio = "",
                    count_rating = 3,
                    average_rating = 5.0,
                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
             services = Services(
                 serviceType = "petSitter", serviceDetail = ServiceDetail(price = 5000, acceptablePets = null, acceptableSize = null, description = null, currencyModel = CurrencyModel(currencyCode = "RUB"), pricePerTime = null)
             )
                )
            )
        items.add(
           User(
                    username = "shafhat",
                    first_name = "Шафхат",
                    last_name = "Руслан",
                    phone = "+77777754722",
                    city = "Алматы",
                    country = "Казахстан",
                    location = "",
                    bio = "",
                    count_rating = 3,
                    average_rating = 4.0,
                    avatar = "https://ru.toluna.com/dpolls_images/2016/02/06/dd190ae0-f595-4a71-b168-5c34dbed1512_x365.jpg",
               services = Services(
                   serviceType = "petSitter", serviceDetail = ServiceDetail(price = 3000, acceptablePets = null, acceptableSize = null, description = null, currencyModel = CurrencyModel(currencyCode = "KZT"), pricePerTime = "час")
               )
                )
            )
        items.add(
            User(
                    username = "margarita",
                    first_name = "Маргарита",
                    last_name = "Поливода",
                    phone = "+77777527722",
                    city = "Алматы",
                    country = "Казахстан",
                    location = "",
                    bio = "",
                    count_rating = 3,
                    average_rating = 4.0,
                    avatar = "https://ru.toluna.com/dpolls_images/2016/02/06/dd190ae0-f595-4a71-b168-5c34dbed1512_x365.jpg",
                services = Services(
                    serviceType = "petSitter", serviceDetail = ServiceDetail(price = 7000, acceptablePets = null, acceptableSize = null, description = null, currencyModel = CurrencyModel(currencyCode = "USD"), pricePerTime = "час")
                )
                )
            )

        result.value = items
        return result
    }
}