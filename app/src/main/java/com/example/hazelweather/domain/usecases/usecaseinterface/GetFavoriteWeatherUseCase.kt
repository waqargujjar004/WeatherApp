package com.example.hazelweather.domain.usecases.usecaseinterface

import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.flow.Flow

interface GetFavoriteWeatherUseCase {
    operator fun invoke(): Flow<List<weather>>
}