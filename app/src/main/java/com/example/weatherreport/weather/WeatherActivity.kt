package com.example.weatherreport.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.weatherreport.R
import com.example.weatherreport.city.CityFragment

class WeatherActivity : AppCompatActivity() {

    private lateinit var weatherFragment: WeatherFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
//
//        weatherFragment = supportFragmentManager.findFragmentById(R.id.weather_fragment) as WeatherFragment

        // Peut remplacer le SetContentView
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, WeatherFragment.newInstance())
            .commit()
    }
}