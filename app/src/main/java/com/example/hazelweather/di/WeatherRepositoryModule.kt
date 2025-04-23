package com.example.hazelweather.di

import com.example.hazelweather.data.local.datasources.LocalDataSource
import com.example.hazelweather.data.remote.datasource.RemoteDataSource
import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.data.repository.WeatherRepositoryImpl

object WeatherRepositoryModule {

    private var weatherRepository: WeatherRepository? = null

    fun provideWeatherRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): WeatherRepository {
        if (weatherRepository == null) {
            weatherRepository = WeatherRepositoryImpl(localDataSource, remoteDataSource)
        }
        return weatherRepository!!
    }
}