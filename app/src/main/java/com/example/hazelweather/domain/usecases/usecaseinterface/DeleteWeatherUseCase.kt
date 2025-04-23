package com.example.hazelweather.domain.usecases.usecaseinterface

import com.example.hazelweather.domain.models.weather

interface DeleteWeatherUseCase {
    suspend operator fun invoke(weather: weather)
}