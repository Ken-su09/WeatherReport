package com.example.weatherreport.city

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherreport.App
import com.example.weatherreport.Database
import com.example.weatherreport.R
import com.example.weatherreport.openweathermap.mapOpenWeatherDataToWeather
import com.example.weatherreport.utils.toast
import com.example.weatherreport.weather.WeatherFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class CityFragment : Fragment(), RecyclerViewCityAdapter.CityItemListener {

    interface CityFragmentListener {
        fun onCitySelected(city: City)
    }

    var listener: CityFragmentListener? = null

    private val TAG = CityFragment::class.java.simpleName

    private lateinit var cities: MutableList<City>
    private lateinit var database: Database
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCityAdapter: RecyclerViewCityAdapter

    private lateinit var floatingActionButton: FloatingActionButton

    private lateinit var myActivity: AppCompatActivity
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = App.database
        cities = mutableListOf()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city, null, false)
        recyclerView = view.findViewById(R.id.cities_recycler_view)
        toolbar = view.findViewById(R.id.fragment_city_toolbar)
        floatingActionButton = view.findViewById(R.id.fragment_city_fab)

        myActivity = (activity as AppCompatActivity)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cities = database.getAllCities()
        recyclerViewCityAdapter = RecyclerViewCityAdapter(cities, this)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewCityAdapter
        }

        with(myActivity) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        checkIfTheCityIsReal()

        floatingActionButton.setOnClickListener {
            showCreateCityAlertDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_city_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_city_menu_button -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreateCityAlertDialog() {
        val createCityDialogFragment = CreateCityDialogFragment()
        createCityDialogFragment.listener =
            object : CreateCityDialogFragment.CreateCityDialogListener {
                override fun onDialogPositiveClick(cityName: String) {
                    saveCity(City(cityName))
                }

                override fun onDialogNegativeClick() {
                }

            }
        fragmentManager?.let { createCityDialogFragment.show(it, "CreateCityDialogFragment") }
    }

    private fun showDeleteCityAlertDialog(city: City) {
        val deleteCityDialogFragment = DeleteCityDialogFragment.newInstance(city.name)
        deleteCityDialogFragment.listener =
            object : DeleteCityDialogFragment.DeleteCityDialogListener {
                override fun onDialogPositiveClick() {
                    deleteCity(city)
                }

                override fun onDialogNegativeClick() {
                }

            }
        fragmentManager?.let { deleteCityDialogFragment.show(it, "DeleteCityDialogFragment") }
    }

    private fun deleteCity(city: City) {
        if (database.deleteCity(city)) {
            cities.remove(city)
            recyclerViewCityAdapter.notifyDataSetChanged()
        } else {
            context?.toast(getString(R.string.toast_message_could_not_delete))
        }
    }

    private fun saveCity(city: City) {
        var cityAlreadyExists = false

        for (c in cities) {
            if (c.name == city.name || c.name == city.name.toUpperCase() || c.name == city.name.capitalize()) {
                cityAlreadyExists = true
                break
            }
        }

        if (cityAlreadyExists) {
            context?.toast(getString(R.string.toast_message_city_already_exists, city.name))
        } else {
            if (database.createCity(city)) {
                cities.add(city)
                recyclerViewCityAdapter.notifyDataSetChanged()
                context?.toast(getString(R.string.toast_message_create_the_city, city.name))
            } else {
                context?.toast(getString(R.string.toast_message_could_not_create))
            }
        }
//    context?.toast(getString(R.string.toast_message_city_is_not_real, city.name)
    }

    override fun onCitySelected(city: City) {
        listener?.onCitySelected(city)
    }

    override fun onCityDeleted(city: City) {
        showDeleteCityAlertDialog(city)
    }

    private fun checkIfTheCityIsReal() {
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://wft-geo-db.p.rapidapi.com/v1/geo/adminDivisions")
            .get()
            .addHeader("x-rapidapi-key", "c28071e9acmsh69b8c44892ecdc8p17e4fcjsnff1e1d455dc7")
            .addHeader("x-rapidapi-host", "wft-geo-db.p.rapidapi.com")
            .build()

        val response = client.newCall(request)
//        response.execute()

        response.enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                response.body().let {
                    Log.i(TAG, "Weather response: $it")

//                    val weather = mapOpenWeatherDataToWeather(it!!)
//                    swipeRefreshLayout.isRefreshing = false
//
//                    updateUi(weather)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }
}