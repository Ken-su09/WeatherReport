package com.example.weatherreport.weather

data class Weather(
    val description: String,
    val temperature: Float,
    val pressure: Int,
    val humidity: Int,
    val iconUrl: String
) {

}
