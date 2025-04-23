package com.example.hazelweather.ui.state

import com.example.hazelweather.domain.models.weather

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val weather: weather) : WeatherState()
    data class Error(val message: String) : WeatherState()
    data class FavoriteStatus(val isFavorite: Boolean) : WeatherState()

    data class WeatherListState(val weatherList: List<weather>) : WeatherState()
}