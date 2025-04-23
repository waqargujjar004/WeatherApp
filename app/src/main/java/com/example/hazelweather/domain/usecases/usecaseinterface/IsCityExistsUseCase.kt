package com.example.hazelweather.domain.usecases.usecaseinterface

interface IsCityExistsUseCase {
    suspend operator fun invoke(cityName: String): Boolean
}