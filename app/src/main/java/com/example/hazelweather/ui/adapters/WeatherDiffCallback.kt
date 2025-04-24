package com.example.hazelweather.ui.adapters

import com.example.hazelweather.domain.models.weather
import com.hazelmobile.cores.bases.adapter.BaseDiffUtils

class WeatherDiffCallback  : BaseDiffUtils<weather>(){
    override fun areItemsTheSame(oldItem: weather, newItem: weather): Boolean {
        return oldItem.name == newItem.name // Compare unique identifier
    }

    override fun areContentsTheSame(oldItem: weather, newItem:weather): Boolean {
           return oldItem.isEqual(newItem)  // Compare full object data

    }
}