package com.example.hazelweather.ui.adapters

import com.example.hazelweather.domain.models.weather
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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







//
//class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
//
//    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    private val differCallback = object : DiffUtil.ItemCallback<weather>() {
//        override fun areItemsTheSame(oldItem: weather, newItem: weather): Boolean {
//            return oldItem.name == newItem.name
//        }
//
//        override fun areContentsTheSame(oldItem:  weather, newItem: weather): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this, differCallback)
//
//    private var onItemClickListener: ((weather) -> Unit)? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_weather, parent, false)
//        return WeatherViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
//        val weather = differ.currentList[position]
//
//        val ivWeatherIcon = holder.itemView.findViewById<ImageView>(R.id.iv_main_weather_icon)
//        val tvCityName = holder.itemView.findViewById<TextView>(R.id.tv_date)
//        val tvTemperature = holder.itemView.findViewById<TextView>(R.id.tv_temperature)
//        val tvWeatherStatus = holder.itemView.findViewById<TextView>(R.id.tv_weather_condition)
//        val tvHumidity = holder.itemView.findViewById<TextView>(R.id.tv_humidity)
//        val tvWindSpeed = holder.itemView.findViewById<TextView>(R.id.tv_wind)
//
//        tvCityName.text = weather.name
//        tvTemperature.text = "${weather.main.temp.roundToInt()}°C"
//        tvWeatherStatus.text = weather.weather.firstOrNull()?.main ?: "N/A"
//        tvHumidity.text = "${weather.main.humidity}%"
//        tvWindSpeed.text = "${weather.wind.speed.roundToInt()} km/h"
//
//        val iconRes = getWeatherIcon(weather.weather.firstOrNull()?.main ?: "")
//        ivWeatherIcon.setImageResource(iconRes)
//
//        holder.itemView.setOnClickListener {
//            onItemClickListener?.invoke(weather)
//        }
//    }
//
//    override fun getItemCount(): Int = differ.currentList.size
//
//    fun setOnItemClickListener(listener: (weather) -> Unit) {
//        onItemClickListener = listener
//    }
//
//    private fun getWeatherIcon(main: String): Int {
//        return when (main.lowercase()) {
//            "clear" -> R.drawable.clear
//            "clouds" -> R.drawable.clouds
//            "rain" -> R.drawable.rain
//            "drizzle" -> R.drawable.drizzle
//            "thunderstorm" -> R.drawable.clouds_moon
//            "snow" -> R.drawable.snow
//            "mist", "fog", "smoke", "haze" -> R.drawable.mist
//            "dust", "sand", "ash" -> R.drawable.clouds_pre
//            "squall", "tornado" -> R.drawable.wind
//            else -> R.drawable.clouds
//        }
//    }
//}
