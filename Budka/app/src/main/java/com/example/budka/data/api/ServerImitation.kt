/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:53
 *
 */

//package com.example.budka.data.api
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.budka.R
//import com.example.budka.data.model.*
//import kotlinx.coroutines.Deferred
//
//class ServerImitation {
//    companion object {
//
//        fun getServiceDetail(serviceId: Int): LiveData<ServiceDetail> {
//            val result = MutableLiveData<ServiceDetail>()
//            val items: MutableList<ServiceDetail> = ArrayList<ServiceDetail>()
//            items.add(
//                ServiceDetail(
//                    service_id = 1,
//                    description = R.string.petSitterDescription.toString(),
//                    acceptablePets = listOf("dog", "cat", "fish"),
//                    acceptableSize = listOf("small", "big"),
//                    additionalProperties = listOf(
//                        Properties("Количество питомцев", "4"),
//                        Properties("Прогулки в день", "3"), Properties("Тип недвижимости", "Дом")
//                    ),
//                    user = PetSitterDetail(
//                        id = 1,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        phone = "+77777777722",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        average_rating = 5.0,
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                        services = listOf(
//                            Services(
//                                id = 2,
//                                serviceType = "petWalking",
//                                4000,
//                                CurrencyModel("KZT"),
//                                "час"
//                            )
//                        ),
//                    )
//                )
//            )
//
//            items.add(
//                ServiceDetail(
//                    service_id = 2,
//                    description = "service id=2",
//                    acceptablePets = listOf("fish"),
//                    acceptableSize = listOf("small"),
//                    additionalProperties = listOf(
//                        Properties("Количество питомцев", "4"),
//                        Properties("Прогулки в день", "3"), Properties("Тип недвижимости", "Дом")
//                    ),
//                    user = PetSitterDetail(
//                        id = 1,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        phone = "+77777777722",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        average_rating = 5.0,
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                        services = listOf(
//                            Services(
//                                id = 2,
//                                serviceType = "petWalking",
//                                4000,
//                                CurrencyModel("KZT"),
//                                "час"
//                            )
//                        ),
//
//                        )
//
//                )
//            )
//            items.add(
//                ServiceDetail(
//                    service_id = 3,
//                    description = "service id=3",
//                    acceptablePets = listOf("fish"),
//                    acceptableSize = listOf("small"),
//                    additionalProperties = listOf(
//                        Properties("Количество питомцев", "4"),
//                        Properties("Прогулки в день", "3"), Properties("Тип недвижимости", "Дом")
//                    ),
//                    user = PetSitterDetail(
//                        id = 1,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        phone = "+77777777722",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        average_rating = 5.0,
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                        services = listOf(
//                            Services(
//                                id = 2,
//                                serviceType = "petWalking",
//                                4000,
//                                CurrencyModel("KZT"),
//                                "час"
//                            )
//                        ),
//
//                        )
//                )
//            )
//            items.forEach {
//                if (it.service_id == serviceId)
//                    result.value = it
//            }
//
//            return result
//
//
//        }
//
//        fun getPetsByUser(userId: Int): MutableList<Pet> {
//            val items: MutableList<Pet> = ArrayList<Pet>()
//            val sortedResult: MutableList<Pet> = ArrayList<Pet>()
//            items.add(
//                Pet(
//                    name = "Ақтөс",
//                    type = 0,
//                    breed = "Овчарка",
//                    weight = 3,
//                    avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
//                    user = PetOwner(
//                        id = 1,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                    )
//                )
//            )
//
//            items.add(
//                Pet(
//                    name = "Акамару",
//                    type = 0,
//                    breed = "Наруто",
//                    weight = 3,
//                    avatar = "https://sun9-36.userapi.com/impg/KhImcrFwCY17mnByO3aepmAihnslaMHm_Gonow/oTCtWPoccTs.jpg?size=449x564&quality=96&sign=4ee8d5995561e6659a32c857c9f17343&type=album",
//                    user = PetOwner(
//                        id = 2,
//                        username = "akimbek",
//                        first_name = "Раслу",
//                        last_name = "Шефхат",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                    )
//                )
//            )
//            items.add(
//                Pet(
//                    name = "Ақтөс",
//                    type = 0,
//                    breed = "Овчарка",
//                    weight = 3,
//                    avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
//                    user = PetOwner(
//                        id = 3,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                    )
//                )
//            )
//            items.forEach {
//                if (it.user?.id == userId) {
//                    sortedResult.add(it)
//                }
//            }
//
//
//
//            return sortedResult
//
//        }
//
//        fun getAllPets(): MutableList<Pet> {
//            val items: MutableList<Pet> = ArrayList<Pet>()
//            items.add(
//                Pet(
//                    name = "Ақтөс",
//                    type = 0,
//                    breed = "Овчарка",
//                    weight = 3,
//                    avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
//                    user = PetOwner(
//                        id = 1,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                    )
//                )
//            )
//
//            items.add(
//                Pet(
//                    name = "Акамару",
//                    type = 0,
//                    breed = "Наруто",
//                    weight = 3,
//                    avatar = "https://sun9-36.userapi.com/impg/KhImcrFwCY17mnByO3aepmAihnslaMHm_Gonow/oTCtWPoccTs.jpg?size=449x564&quality=96&sign=4ee8d5995561e6659a32c857c9f17343&type=album",
//                    user = PetOwner(
//                        id = 2,
//                        username = "akimbek",
//                        first_name = "Раслу",
//                        last_name = "Шефхат",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                    )
//                )
//            )
//            items.add(
//                Pet(
//                    name = "Ақтөс",
//                    type = 0,
//                    breed = "Овчарка",
//                    weight = 3,
//                    avatar = "https://petsi.net/images/dogbreed/big/10.jpg",
//                    user = PetOwner(
//                        id = 3,
//                        username = "akimbek",
//                        first_name = "Акимбек",
//                        last_name = "Байтасов",
//                        city = "Almaty",
//                        country = "Kazakhstan",
//                        location = "",
//                        avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                    )
//                )
//            )
//
//
//            return items
//
//        }
//
//        fun getPetSitters(): List<PetSitter> {
//            val result = MutableLiveData<List<PetSitter>>()
//            val items: MutableList<PetSitter> = ArrayList<PetSitter>()
//            items.add(
//                PetSitter(
//                    id = 1,
//                    username = "akimbek",
//                    first_name = "Акимбек",
//                    last_name = "Байтасов",
//                    city = "Almaty",
//                    country = "Kazakhstan",
//                    location = "",
//                    average_rating = 5.0,
//                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                )
//            )
//
//            items.add(
//                PetSitter(
//                    id = 2,
//                    username = "akimbek2",
//                    first_name = "Акимбек2",
//                    last_name = "Байтасов2",
//                    city = "Almaty",
//                    country = "Kazakhstan",
//                    location = "",
//                    average_rating = 4.0,
//                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                )
//            )
//            items.add(
//                PetSitter(
//                    id = 3,
//                    username = "akimbek3",
//                    first_name = "Акимбек3",
//                    last_name = "Байтасов3",
//                    city = "Almaty",
//                    country = "Kazakhstan",
//                    location = "",
//                    average_rating = 4.0,
//                    avatar = "https://tengrinews.kz/userdata/news/2020/news_414968/thumb_b/photo_336922.png",
//                )
//            )
//            result.value = items
//
//
//            return items
//        }
//    }
//}