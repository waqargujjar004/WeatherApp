package com.example.hazelweather.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hazelweather.domain.models.weather

@Database(
    entities = [weather::class],  // Include the WeatherEntity here
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)  // Register the TypeConverters
abstract class WeatherDatabase : RoomDatabase() {

    // Provide access to the Weather DAO
    abstract fun getWeatherDao(): WeatherDao
}