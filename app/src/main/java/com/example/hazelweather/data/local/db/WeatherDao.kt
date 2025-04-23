package com.example.hazelweather.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hazelweather.domain.models.weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    // Insert or replace weather data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: weather): Long

    // Get all saved weather data (e.g., from favorite cities)
    @Query("SELECT * FROM weather_data")
    fun getAllWeather(): Flow<List<weather>>

    // Delete weather data
    @Delete
    suspend fun deleteWeather(weather: weather)

    // Check if weather for a city exists in the database
    @Query("SELECT EXISTS(SELECT 1 FROM weather_data WHERE name = :cityName)")
    suspend fun isWeatherExist(cityName: String): Boolean
}