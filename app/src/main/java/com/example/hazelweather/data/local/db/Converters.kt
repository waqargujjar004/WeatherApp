package com.example.hazelweather.data.local.db

import androidx.room.TypeConverter
import com.example.hazelweather.domain.models.Clouds
import com.example.hazelweather.domain.models.Coord
import com.example.hazelweather.domain.models.Main
import com.example.hazelweather.domain.models.Sys
import com.example.hazelweather.domain.models.WeatherX
import com.example.hazelweather.domain.models.Wind
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromWeatherList(weatherList: List<WeatherX>?): String? {
        return Gson().toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(weatherString: String?): List<WeatherX>? {
        return Gson().fromJson(weatherString, Array<WeatherX>::class.java)?.toList()
    }

    @TypeConverter
    fun fromCoord(coord: Coord?): String? {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toCoord(coordString: String?): Coord? {
        return Gson().fromJson(coordString, Coord::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind?): String? {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(windString: String?): Wind? {
        return Gson().fromJson(windString, Wind::class.java)
    }

    @TypeConverter
    fun fromSys(sys: Sys?): String? {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(sysString: String?): Sys? {
        return Gson().fromJson(sysString, Sys::class.java)
    }

    @TypeConverter
    fun fromClouds(clouds: Clouds?): String? {
        return Gson().toJson(clouds)
    }
    @TypeConverter
    fun fromMain(main: Main?): String? {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(mainString: String?): Main? {
        return Gson().fromJson(mainString, Main::class.java)
    }
    @TypeConverter
    fun toClouds(cloudsString: String?): Clouds? {
        return Gson().fromJson(cloudsString, Clouds::class.java)
    }
}