package com.example.hazelweather.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hazelweather.di.AppModule
import com.example.hazelweather.domain.usecases.usecaseImpl.DeleteWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.GetCurrentWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.GetFavoriteWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.IsCityExistsUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseImpl.SaveWeatherUseCaseImpl
import com.example.hazelweather.domain.usecases.usecaseinterface.DeleteWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.GetCurrentWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.GetFavoriteWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.IsCityExistsUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.SaveWeatherUseCase
import com.example.hazelweather.util.NetworkHelper

class WeatherViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    // Initialize UseCases and other dependencies from AppModule
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase by lazy {
        GetCurrentWeatherUseCaseImpl(AppModule.weatherRepository)
    }

    private val saveWeatherUseCase: SaveWeatherUseCase by lazy {
        SaveWeatherUseCaseImpl(AppModule.weatherRepository)
    }

    private val getFavoriteWeatherUseCase: GetFavoriteWeatherUseCase by lazy {
        GetFavoriteWeatherUseCaseImpl(AppModule.weatherRepository)
    }

    private val deleteWeatherUseCase: DeleteWeatherUseCase by lazy {
        DeleteWeatherUseCaseImpl(AppModule.weatherRepository)
    }

    private val isCityExistsUseCase: IsCityExistsUseCase by lazy {
        IsCityExistsUseCaseImpl(AppModule.weatherRepository)
    }
    private val networkHelper: NetworkHelper by lazy {
        NetworkHelper(context)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(
                getCurrentWeatherUseCase,
                saveWeatherUseCase,
                getFavoriteWeatherUseCase,
                deleteWeatherUseCase,
                isCityExistsUseCase,
                networkHelper

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
