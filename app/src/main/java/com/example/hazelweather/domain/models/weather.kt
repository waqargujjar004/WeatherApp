package com.example.hazelweather.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hazelweather.data.local.db.Converters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "weather_data")
@Parcelize
data class weather(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @TypeConverters(Converters::class)
    val base: String,
    @TypeConverters(Converters::class)
    val clouds: Clouds,
    val cod: Int,
    @TypeConverters(Converters::class)
    val coord: Coord,
    val dt: Int,
    @TypeConverters(Converters::class)
    val main: Main,
    val name: String,
    @TypeConverters(Converters::class)
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    @TypeConverters(Converters::class)
    val weather: List<WeatherX>,
    @TypeConverters(Converters::class)
    val wind: Wind
) : Parcelable {
    fun isEqual(other: weather): Boolean {
        return base == other.base &&
                clouds == other.clouds &&
                cod == other.cod &&
                coord == other.coord &&
                dt == other.dt &&
                main == other.main &&
                name == other.name &&
                sys == other.sys &&
                timezone == other.timezone &&
                visibility == other.visibility &&
                weather == other.weather &&
                wind == other.wind
    }
}