package com.example.weatherreport.openweathermap

import com.example.weatherreport.weather.Weather

fun mapOpenWeatherDataToWeather(weatherWrapper: WeatherWrapper): Weather {
    val weatherFirst = weatherWrapper.weather.first()
    val main = weatherWrapper.main

    return Weather(
        description = weatherFirst.description,
        temperature = main.temperature,
        pressure = main.pressure,
        humidity = main.humidity,
        iconUrl = "https://openweathermap.org/img/w/${weatherFirst.icon}.png"
    )
}