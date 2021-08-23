package com.example.weatherreport.weather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherreport.App
import com.example.weatherreport.R
import com.example.weatherreport.city.CityActivity
import com.example.weatherreport.openweathermap.OpenWeatherService
import com.example.weatherreport.openweathermap.WeatherWrapper
import com.example.weatherreport.openweathermap.mapOpenWeatherDataToWeather
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class WeatherFragment : Fragment() {

    private val TAG = WeatherFragment::class.java.simpleName
    private lateinit var cityName: String

    private lateinit var city: TextView
    private lateinit var weatherIcon: AppCompatImageView
    private lateinit var weatherDescription: TextView
    private lateinit var weatherTemperature: TextView
    private lateinit var weatherPressure: TextView
    private lateinit var weatherHumidity: TextView

    private lateinit var myActivity: AppCompatActivity
    private var toolbar: Toolbar? = null

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object {
        const val EXTRA_CITY_NAME = "com.example.weatherreport.extras.EXTRA_CITY_NAME"

        fun newInstance() = WeatherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        myActivity = (activity as AppCompatActivity)

        city = view.findViewById(R.id.fragment_weather_city_name)
        weatherIcon = view.findViewById(R.id.fragment_weather_image)
        weatherDescription = view.findViewById(R.id.fragment_weather_description)
        weatherTemperature = view.findViewById(R.id.temperature_data)
        weatherPressure = view.findViewById(R.id.pressure_data)
        weatherHumidity = view.findViewById(R.id.humidity_data)

        toolbar = view.findViewById(R.id.fragment_weather_toolbar)

        swipeRefreshLayout = view.findViewById(R.id.fragment_weather_swipe_refresh_layout)

        swipeRefreshLayout.setOnRefreshListener { refreshWeather() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity?.intent!!.hasExtra(EXTRA_CITY_NAME)) {
            cityName = activity!!.intent.getStringExtra(EXTRA_CITY_NAME)!!
            updateWeatherForCity(activity!!.intent.getStringExtra(EXTRA_CITY_NAME)!!)
        }

        with(myActivity) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            myActivity.supportActionBar!!.title = cityName
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_weather_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_city_menu_button -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUi(weather: Weather) {
        Picasso.with(context).load(weather.iconUrl).into(weatherIcon)

        city.text = cityName
        weatherDescription.text = weather.description.capitalize()
        weatherTemperature.text =
            getString(R.string.fragment_weather_temperature_data, weather.temperature.toInt())
        weatherPressure.text = getString(R.string.fragment_weather_pressure_data, weather.pressure)
        weatherHumidity.text = getString(R.string.fragment_weather_humidity_data, weather.humidity)
    }

    private fun refreshWeather() {
        updateWeatherForCity(cityName)
    }

    private fun updateWeatherForCity(cityName: String) {
        if (!swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = true
        }

        val call = App.weatherService.getWeather(cityName, OpenWeatherService.API_KEY)
        call.enqueue(object : retrofit2.Callback<WeatherWrapper> {
            override fun onResponse(
                call: Call<WeatherWrapper>,
                response: Response<WeatherWrapper>
            ) {
                response.body().let {
                    val weather = mapOpenWeatherDataToWeather(it!!)
                    Log.i(TAG, "Weather response: $weather")
                    swipeRefreshLayout.isRefreshing = false

                    updateUi(weather)
                }
            }

            override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                Log.e(TAG, getString(R.string.weather_fragment_failure_call), t)
                Toast.makeText(
                    activity,
                    getString(R.string.weather_fragment_failure_call),
                    Toast.LENGTH_SHORT
                ).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }
}