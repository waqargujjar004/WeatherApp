package com.example.hazelweather.ui
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hazelweather.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class WeatherUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            Log.d("WeatherUpdateWorker", "Worker is starting...")

            val getFavorites = AppModule.getFavoriteWeatherUseCase
            val getCurrentWeather = AppModule.getCurrentWeatherUseCase
            val saveWeather = AppModule.saveWeatherUseCase

            // Log the retrieved favorites
            val savedCities = getFavorites().first()
            Log.d("WeatherUpdateWorker", "Retrieved saved cities: ${savedCities.size}")

            if (savedCities.isEmpty()) {
                Log.d("WeatherUpdateWorker", "No favorite cities found")
            }

            // Iterate over the list and update weather for each city
            savedCities.forEach { city ->
                Log.d("WeatherUpdateWorker", "Updating weather for city: ${city.name}")
                val response = getCurrentWeather(city.name)
                Log.d("WeatherUpdateWorker", "Weather update response: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    saveWeather(response.body()!!)
                    Log.d("WeatherUpdateWorker", "Weather updated and saved for ${city.name}")
                } else {
                    Log.d("WeatherUpdateWorker", "Failed to get weather for ${city.name}")
                }
            }

            return@withContext Result.success()

        } catch (e: Exception) {
            Log.e("WeatherUpdateWorker", "Error occurred: ${e.message}")
            e.printStackTrace()
            return@withContext Result.failure()
        }
    }
}