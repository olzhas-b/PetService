package com.example.budka.data.model


data class ServiceDetail(
    val service_id: Int,
    val description: String?,
    val acceptablePets: List<String>?,
    val acceptableSize: List<String>?,
    val additionalProperties: List<Properties>?,
    val user: PetSitterDetail?
)

data class Properties(
    val label: String?,
    val text: String?
)