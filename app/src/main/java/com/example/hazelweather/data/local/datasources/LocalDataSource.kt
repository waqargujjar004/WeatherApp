package com.example.hazelweather.data.local.datasources

import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveWeather(weather: weather): Long
    fun getSavedWeather(): Flow<List<weather>>
    suspend fun deleteWeather(weather: weather)
    suspend fun isWeatherExists(cityName: String): Boolean
}
