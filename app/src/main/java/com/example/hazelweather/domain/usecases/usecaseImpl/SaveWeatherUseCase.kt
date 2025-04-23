package com.example.hazelweather.domain.usecases.usecaseImpl

import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.domain.usecases.usecaseinterface.SaveWeatherUseCase

class SaveWeatherUseCaseImpl(
    private val repository: WeatherRepository
) : SaveWeatherUseCase {
   override suspend operator fun invoke(weather: weather): Long {
        return repository.saveWeather(weather)
    }
}