package com.example.hazelweather.data.remote.api

import retrofit2.http.Query
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.util.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.Response

interface WeatherApi {


    @GET("weather")
    suspend fun getCurrentWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY, // Make sure to pass API key manually
        @Query("units") units: String = "metric"
    ): Response<weather>
}