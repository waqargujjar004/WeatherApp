package com.example.hazelweather.di

import com.example.hazelweather.data.remote.datasource.RemoteDataSource
import com.example.hazelweather.data.remote.datasource.RemoteDataSourceImpl

object DataSourceModule {


        private val remoteDataSource: RemoteDataSource by lazy {
            RemoteDataSourceImpl(NetworkModule.weatherApi)
        }

        fun provideRemoteDataSource() = remoteDataSource

}