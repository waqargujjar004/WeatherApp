package com.example.hazelweather.data.repository

import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): Response<weather>
    suspend fun saveWeather(weather: weather): Long
    fun getFavoriteWeather(): Flow<List<weather>>
    suspend fun deleteWeather(weather: weather)
    suspend fun isCityExists(cityName: String): Boolean
}