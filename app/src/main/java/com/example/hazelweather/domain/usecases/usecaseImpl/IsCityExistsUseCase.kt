package com.example.hazelweather.domain.usecases.usecaseImpl

import com.example.hazelweather.data.repository.WeatherRepository
import com.example.hazelweather.domain.usecases.usecaseinterface.IsCityExistsUseCase

class IsCityExistsUseCaseImpl(
    private val repository: WeatherRepository
): IsCityExistsUseCase {
   override suspend operator fun invoke(cityName: String): Boolean {
        return repository.isCityExists(cityName)
    }
}