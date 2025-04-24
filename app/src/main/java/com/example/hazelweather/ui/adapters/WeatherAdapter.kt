package com.example.hazelweather.ui.adapters

import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.R
import com.example.hazelweather.databinding.ItemWeatherBinding
import com.hazelmobile.core.bases.adapter.BaseAdapter
import kotlin.math.roundToInt


class WeatherAdapter :
    BaseAdapter<weather, ItemWeatherBinding>(ItemWeatherBinding::inflate, WeatherDiffCallback()) {

    override fun ItemWeatherBinding.bindViews(model: weather) {
        tvDate.text = model.name
        tvTemperature.text = "${model.main.temp.roundToInt()}°C"
        tvWeatherCondition.text = model.weather.firstOrNull()?.main ?: "N/A"
        tvHumidity.text = "${model.main.humidity}%"
        tvWind.text = "${model.wind.speed.roundToInt()} km/h"

        val iconRes = getWeatherIcon(model.weather.firstOrNull()?.main ?: "")
        ivMainWeatherIcon.setImageResource(iconRes)
    }

    override fun ItemWeatherBinding.bindListeners(item: weather) {
        root.setOnClickListener {
            onItemCallback?.invoke(item)
        }
    }

    private fun getWeatherIcon(main: String): Int {
        return when (main.lowercase()) {
            "clear" -> R.drawable.clear
            "clouds" -> R.drawable.clouds
            "rain" -> R.drawable.rain
            "drizzle" -> R.drawable.drizzle
            "thunderstorm" -> R.drawable.clouds_moon
            "snow" -> R.drawable.snow
            "mist", "fog", "smoke", "haze" -> R.drawable.mist
            "dust", "sand", "ash" -> R.drawable.clouds_pre
            "squall", "tornado" -> R.drawable.wind
            else -> R.drawable.clouds
        }
    }
}







