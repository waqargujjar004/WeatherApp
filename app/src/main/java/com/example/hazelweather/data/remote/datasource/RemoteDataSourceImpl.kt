package com.example.hazelweather.data.remote.datasource


import com.example.hazelweather.data.remote.api.WeatherApi
import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RemoteDataSourceImpl(
    private val weatherApi: WeatherApi
) : RemoteDataSource {

    override suspend fun getCurrentWeatherByCity(cityName: String): Response<weather> {
        return withContext(Dispatchers.IO) {
            weatherApi.getCurrentWeatherByCity(cityName)
        }
    }
}