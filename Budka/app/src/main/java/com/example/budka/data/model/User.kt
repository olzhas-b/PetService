package com.example.budka.data.model

data class User(
    val username: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val city: String,
    val country: String,
    val location: String,
    val bio: String,
    val count_rating: Int,
    val average_rating: Double,
    val avatar: String,
    val services: Services?

)

data class Services(
    val serviceType: String,
    val serviceDetail: ServiceDetail
)

data class ServiceDetail(
    val description: String?,
    val acceptablePets: List<String>?,
    val acceptableSize: List<String>?,
    val price: Int,
    val currencyModel: CurrencyModel,
    val pricePerTime: String?
)

