package com.example.hazelweather.domain.usecases.usecaseImpl

import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.domain.usecases.usecaseinterface.GetCurrentWeatherUseCase
import retrofit2.Response

class GetCurrentWeatherUseCaseImpl(
    private val repository: WeatherRepository
) : GetCurrentWeatherUseCase {
   override suspend operator fun invoke(city: String): Response<weather> {
        return repository.getCurrentWeather(city)
    }
}
