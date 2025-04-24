package com.example.hazelweather

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hazelweather.di.AppModule
import com.example.hazelweather.ui.WeatherUpdateWorker
import java.util.concurrent.TimeUnit

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize the AppModule
        AppModule.initialize(this)
        setupWeatherWorker()

    }
    private fun setupWeatherWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

//        val testWork = OneTimeWorkRequestBuilder<WeatherUpdateWorker>()
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance(this).enqueue(testWork)

        val workRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(
            1, TimeUnit.HOURS // runs every 6 hours
        )


            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "weather_update_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}