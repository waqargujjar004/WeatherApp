package com.example.hazelweather.di

import android.content.Context
import androidx.room.Room
import com.example.hazelweather.data.local.db.WeatherDao
import com.example.hazelweather.data.local.db.WeatherDatabase

object DatabaseModule {

    private var weatherDatabase: WeatherDatabase? = null

    fun provideDatabase(context: Context): WeatherDatabase {
        if (weatherDatabase == null) {
            weatherDatabase = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather_db.db"
            ).fallbackToDestructiveMigration().build()
        }
        return weatherDatabase!!
    }

    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.getWeatherDao()
    }
}