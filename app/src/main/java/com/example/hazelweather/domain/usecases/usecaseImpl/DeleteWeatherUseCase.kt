package com.example.hazelweather.domain.usecases.usecaseImpl

import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.domain.usecases.usecaseinterface.DeleteWeatherUseCase

class DeleteWeatherUseCaseImpl(
    private val repository: WeatherRepository
): DeleteWeatherUseCase {
   override suspend operator fun invoke(weather: weather) {
        repository.deleteWeather(weather)
    }
}
