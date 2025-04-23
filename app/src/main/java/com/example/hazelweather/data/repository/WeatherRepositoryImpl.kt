package com.example.hazelweather.data.repository

import com.example.hazelweather.data.local.datasources.LocalDataSource
import com.example.hazelweather.data.remote.datasource.RemoteDataSource
import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {

    override suspend fun getCurrentWeather(city: String): Response<weather> =
        remoteDataSource.getCurrentWeatherByCity(city)

    override suspend fun saveWeather(weather: weather): Long =
        localDataSource.saveWeather(weather)

    override fun getFavoriteWeather(): Flow<List<weather>> =
        localDataSource.getSavedWeather()

    override suspend fun deleteWeather(weather: weather) =
        localDataSource.deleteWeather(weather)

    override suspend fun isCityExists(cityName: String): Boolean =
        localDataSource.isWeatherExists(cityName)
}