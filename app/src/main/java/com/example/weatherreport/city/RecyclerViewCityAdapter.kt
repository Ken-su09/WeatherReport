package com.example.weatherreport.city

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherreport.R

class RecyclerViewCityAdapter(
    private val cities: MutableList<City>, private val cityItemListener: CityItemListener
) : RecyclerView.Adapter<RecyclerViewCityAdapter.ViewHolder>(), View.OnClickListener {

    interface CityItemListener {
        fun onCitySelected(city: City)
        fun onCityDeleted(city: City)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_city, null, false))

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        with(holder) {
            itemCityCardView.tag = city
            itemCityText.text = city.name
            itemCityCardView.setOnClickListener(this@RecyclerViewCityAdapter)
            itemCityDeleteCity.tag = city
            itemCityDeleteCity.setOnClickListener(this@RecyclerViewCityAdapter)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemCityCardView = view.findViewById<CardView>(R.id.item_city_card_view)
        var itemCityIcon = view.findViewById<AppCompatImageView>(R.id.item_city_icon)
        var itemCityDeleteCity = view.findViewById<AppCompatImageView>(R.id.item_city_delete)
        var itemCityText = view.findViewById<TextView>(R.id.item_city_text)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.item_city_card_view -> cityItemListener.onCitySelected(v.tag as City)
            R.id.item_city_delete -> cityItemListener.onCityDeleted(v.tag as City)
        }
    }
}