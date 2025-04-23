package com.example.hazelweather.di

import com.example.hazelweather.data.local.datasources.LocalDataSource
import com.example.hazelweather.data.local.datasources.LocalDataSourceImpl
import com.example.hazelweather.data.local.db.WeatherDao

object LocalDataSourceModule {

    private var localDataSource: LocalDataSource? = null

    fun provideLocalDataSource(weatherDao: WeatherDao): LocalDataSource {
        if (localDataSource == null) {
            localDataSource = LocalDataSourceImpl(weatherDao)
        }
        return localDataSource!!
    }
}
