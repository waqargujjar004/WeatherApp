package com.example.hazelweather.data.local.datasources

import com.example.hazelweather.data.local.db.WeatherDao
import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(
    private val weatherDao: WeatherDao
) : LocalDataSource {

    override suspend fun saveWeather(weather: weather): Long {
        return withContext(Dispatchers.IO) {
            weatherDao.insert(weather)
        }
    }

    override fun getSavedWeather(): Flow<List<weather>> {
        return weatherDao.getAllWeather()
    }

    override suspend fun deleteWeather(weather: weather) {
        withContext(Dispatchers.IO) {
            weatherDao.deleteWeather(weather)
        }
    }

    override suspend fun isWeatherExists(cityName: String): Boolean {
        return withContext(Dispatchers.IO) {
            weatherDao.isWeatherExist(cityName)
        }
    }
}
