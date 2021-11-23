package com.example.budka.data.model

data class Pet(
    val name: String,
    val type: Int,
    val breed: String,
    val weight: Int,
    val avatar: String,
    val user: PetOwner?
)

data class PetResponse(
    val pets: List<Pet>
)
