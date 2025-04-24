package com.example.hazelweather.ui.events

import com.example.hazelweather.domain.models.weather

sealed class WeatherEvent {
    data class FetchWeather(val city: String) : WeatherEvent()
    data class SaveWeather(val weather: weather) : WeatherEvent()
    data class CheckFavoriteStatus(val cityName: String) : WeatherEvent()
    data class DeleteWeather(val weather: weather) : WeatherEvent()

}