package com.example.hazelweather.data.remote.datasource

import com.example.hazelweather.domain.models.weather
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getCurrentWeatherByCity(cityName: String): Response<weather>
}