package com.example.budka.data.model

data class Countries (
    var error: Boolean,
    var msg: String,
    var data : List<CountryData>,
    )

data class CountryData(
    var country: String,
    var cities: List<String>
)