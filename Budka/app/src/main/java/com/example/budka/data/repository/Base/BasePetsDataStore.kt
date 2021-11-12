package com.example.budka.data.repository.Base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budka.data.model.*

abstract class BasePetsDataStore {
    abstract fun loadData(): LiveData<List<Pet>>

    inline fun fetchData(): LiveData<List<Pet>> {
        val result = MutableLiveData<List<Pet>>()
        val items: MutableList<Pet> = ArrayList<Pet>()
        items.add(
            Pet(
                name = "Ақтөс",
                type = 0,
                breed = "Овчарка",
                weight = 3,
                avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
                user = User(
                    id=1,
                    username = "akimbek",
                    first_name = "Акимбек",
                    last_name = "Байтасов",
                    phone = "+77777777722",
                    city = "Almaty",
                    country = "Kazakhstan",
                    location = "",
                    bio = "",
                    count_rating = 3,
                    average_rating = 5.0,
                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
                    services = Services(id=1,
                        serviceType = "petSitter", serviceDetail = ServiceDetail(price = 3000, acceptablePets = null, acceptableSize = null, description = null, currencyModel = CurrencyModel(currencyCode = "KZT"), pricePerTime = "час"),
                    )
                )
            )
        )
        items.add(
            Pet(
                name = "Ақтөс",
                type = 0,
                breed = "Овчарка",
                weight = 3,
                avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
                user = User(
                    id=2,
                    username = "akimbek",
                    first_name = "Акимбек",
                    last_name = "Байтасов",
                    phone = "+77777777722",
                    city = "Almaty",
                    country = "Kazakhstan",
                    location = "",
                    bio = "",
                    count_rating = 3,
                    average_rating = 5.0,
                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
                    services = Services(id=2,
                        serviceType = "petSitter", serviceDetail = ServiceDetail(price = 4000, acceptablePets = null, acceptableSize = null, description = null, currencyModel = CurrencyModel(currencyCode = "RUB"), pricePerTime = "час")
                    )
                )
            )
        )
        items.add(
            Pet(
                name = "Ақтөс",
                type = 0,
                breed = "Овчарка",
                weight = 3,
                avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
                user = User(
                    id=3,
                    username = "akimbek",
                    first_name = "Акимбек",
                    last_name = "Байтасов",
                    phone = "+77777777722",
                    city = "Almaty",
                    country = "Kazakhstan",
                    location = "",
                    bio = "",
                    count_rating = 3,
                    average_rating = 5.0,
                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
                    services = Services(id=3,
                        serviceType = "petSitter", serviceDetail = ServiceDetail(price = 3000, acceptablePets = null, acceptableSize = null, description = null, currencyModel = CurrencyModel(currencyCode = "USD"), pricePerTime = "час")
                    )
                )
            )
        )
        result.value = items
        return result
    }
}