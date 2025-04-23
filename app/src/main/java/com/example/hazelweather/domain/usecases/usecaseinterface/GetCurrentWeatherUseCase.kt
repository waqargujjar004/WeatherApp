package com.example.hazelweather.domain.usecases.usecaseinterface

import com.example.hazelweather.domain.models.weather
import retrofit2.Response

interface GetCurrentWeatherUseCase {
    suspend operator fun invoke(city: String): Response<weather>
}