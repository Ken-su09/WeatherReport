package com.example.weatherreport.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    companion object {
        const val API_KEY = "71004e2b438c9ddc496cc2236bf53418"
    }

    @GET("data/2.5/weather?&units=metric&lang=fr")
    fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Call<WeatherWrapper>

//    "http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=$API_KEY"
}