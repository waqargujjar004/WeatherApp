package com.example.hazelweather.domain.usecases.usecaseImpl

import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.domain.usecases.usecaseinterface.GetFavoriteWeatherUseCase
import kotlinx.coroutines.flow.Flow

class GetFavoriteWeatherUseCaseImpl(
    private val repository: WeatherRepository
) : GetFavoriteWeatherUseCase {
    override operator fun invoke(): Flow<List<weather>> {
        return repository.getFavoriteWeather()
    }
}
