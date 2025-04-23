package com.example.hazelweather.di

import android.content.Context
import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.domain.usecases.usecaseImpl.DeleteWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.GetCurrentWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.GetFavoriteWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.IsCityExistsUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.SaveWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseinterface.DeleteWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.GetCurrentWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.GetFavoriteWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.IsCityExistsUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.SaveWeatherUseCase


object AppModule {


    lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    lateinit var saveWeatherUseCase: SaveWeatherUseCase
    lateinit var getFavoriteWeatherUseCase: GetFavoriteWeatherUseCase
    lateinit var deleteWeatherUseCase: DeleteWeatherUseCase
    lateinit var isCityExistsUseCase: IsCityExistsUseCase
    lateinit var weatherRepository: WeatherRepository

    fun initialize(context: Context) {
        // Get WeatherDao from the DatabaseModule
        val weatherDao = DatabaseModule.provideWeatherDao(
            DatabaseModule.provideDatabase(context)
        )

        // Get WeatherApi from the NetworkModule
       // val weatherApi = NetworkModule.provideWeatherApi()

        // Provide Local and Remote Data Sources
        val localDataSource = LocalDataSourceModule.provideLocalDataSource(weatherDao)
        val remoteDataSource = DataSourceModule.provideRemoteDataSource()

        // Provide WeatherRepository
        weatherRepository = WeatherRepositoryModule.provideWeatherRepository(
            localDataSource, remoteDataSource


        )
        getCurrentWeatherUseCase = GetCurrentWeatherUseCaseImpl(weatherRepository)
        saveWeatherUseCase = SaveWeatherUseCaseImpl(weatherRepository)
        getFavoriteWeatherUseCase = GetFavoriteWeatherUseCaseImpl(weatherRepository)
        deleteWeatherUseCase = DeleteWeatherUseCaseImpl(weatherRepository)
        isCityExistsUseCase = IsCityExistsUseCaseImpl(weatherRepository)
    }
}