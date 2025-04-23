package com.example.hazelweather.domain.usecases.usecaseinterface

import com.example.hazelweather.domain.models.weather

interface SaveWeatherUseCase {
    suspend operator fun invoke(weather: weather): Long
}