package com.example.hazelweather.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.domain.usecases.usecaseinterface.DeleteWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.GetCurrentWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.GetFavoriteWeatherUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.IsCityExistsUseCase
import com.example.hazelweather.domain.usecases.usecaseinterface.SaveWeatherUseCase
import com.example.hazelweather.ui.events.WeatherEvent
import com.example.hazelweather.ui.state.WeatherState
import com.example.hazelweather.util.NetworkHelper
import com.hazelmobile.cores.bases.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val saveWeatherUseCase: SaveWeatherUseCase,
    private val getFavoriteWeatherUseCase: GetFavoriteWeatherUseCase,
    private val deleteWeatherUseCase: DeleteWeatherUseCase,
    private val isCityExistsUseCase: IsCityExistsUseCase,
    private val networkHelper: NetworkHelper
) : BaseViewModel<WeatherEvent, weather>() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> get() = _state

     override fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.FetchWeather -> fetchWeather(event.city)
            is WeatherEvent.SaveWeather -> {
                saveWeatherToDb(event.weather)
            }
            is WeatherEvent.CheckFavoriteStatus -> {
                checkFavoriteStatus(event.cityName)
            }
            is WeatherEvent.DeleteWeather -> {
                deleteWeather(event.weather)
            }

        }
    }

    private fun fetchWeather(city: String) {


        viewModelScope.launch(Dispatchers.IO) {
            if (!networkHelper.hasInternetConnection()) {
                withContext(Dispatchers.Main) {
                    _state.value = WeatherState.Error("No internet connection")
                }
                return@launch
            }

            withContext(Dispatchers.Main) { _state.value = WeatherState.Loading }
            try {
                val response = getCurrentWeatherUseCase(city)
                if (response.isSuccessful && response.body() != null) {
                    val weather = response.body()!!
                    withContext(Dispatchers.Main) {
                        _state.value = WeatherState.Success(weather)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _state.value = WeatherState.Error(response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _state.value = WeatherState.Error(e.message ?: "Unknown error")
                }
            }
        }

    }

    private fun saveWeatherToDb(weather: weather) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveWeatherUseCase(weather)

            } catch (e: Exception) {
                _state.value = WeatherState.Error("Failed to save weather.")
            }
        }
    }
    private fun checkFavoriteStatus(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = isCityExistsUseCase(cityName)
            withContext(Dispatchers.Main) {
                _state.value = WeatherState.FavoriteStatus(isFavorite) // UI update on main thread
            }
        }
    }

    fun getFavoriteWeather() {

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = WeatherState.Loading
            try {

                getFavoriteWeatherUseCase().collect { favoriteWeatherList ->
                    withContext(Dispatchers.Main) {
                        if (favoriteWeatherList.isNotEmpty()) {
                            _state.value =
                                WeatherState.WeatherListState(favoriteWeatherList.toList())  // Use the new state class here
                        } else {
                            _state.value = WeatherState.WeatherListState(favoriteWeatherList.toList())
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = WeatherState.Error("Error fetching favorites: ${e.message}")
            }
        }
    }
    private fun deleteWeather(weather: weather) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteWeatherUseCase(weather)

                withContext(Dispatchers.Main) {
                    getFavoriteWeather() // UI update on main thread
                }
            } catch (e: Exception) {
              _state.value = WeatherState.Error("Failed to delete weather.")
            }
        }
    }
}
