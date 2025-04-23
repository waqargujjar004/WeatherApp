package com.example.hazelweather.ui.fragments

import android.content.pm.PackageManager
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hazelweather.R
import com.example.hazelweather.databinding.FragmentHomeBinding
import com.example.hazelweather.ui.events.WeatherEvent
import com.example.hazelweather.ui.state.WeatherState
import com.example.hazelweather.ui.viewmodel.WeatherViewModel
import com.example.hazelweather.ui.viewmodel.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Locale
import android.Manifest
import androidx.core.view.isVisible
import com.example.hazelweather.domain.models.weather
import com.hazelmobile.cores.bases.fragment.BaseFragment
import kotlin.math.roundToInt



class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var weatherViewModel: WeatherViewModel
    private var isFavorite: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var lastFetchedCityName: String = ""


    override fun FragmentHomeBinding.bindViews() {
        initViewModel()
        setupLocationClient()
        checkLocationPermissionAndFetchWeather()
    }

    override fun FragmentHomeBinding.bindListeners() {
        setupSearchListener()
    }

    override fun FragmentHomeBinding.bindObservers() {
        observeWeatherState()
    }

    private fun initViewModel() {
        val factory = WeatherViewModelFactory(requireContext())
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun setupSearchListener() {
        binding?.itemError?.retryButton?.setOnClickListener {
            weatherViewModel.onEvent(WeatherEvent.FetchWeather(lastFetchedCityName))
        }
    }

    private fun observeWeatherState() {
        viewLifecycleOwner.lifecycleScope.launch {
            weatherViewModel.state.collect { state ->
                when (state) {
                    is WeatherState.Loading -> {
                        binding?.weatherDataLayout?.visibility = View.GONE
                        binding?.paginationProgressBar?.visibility = View.VISIBLE
                        binding?.loadingScreen?.visibility = View.VISIBLE

                    }

                    is WeatherState.Success -> {
                        binding?.root?.findViewById<View>(R.id.itemError)?.isVisible  = false
                        binding?.paginationProgressBar?.visibility = View.INVISIBLE
                        binding?.loadingScreen?.visibility = View.INVISIBLE
                        binding?.weatherDataLayout?.visibility = View.VISIBLE
                        updateWeatherUI(state)
                        setupFabListener(state.weather)
                    }

                    is WeatherState.Error -> {

                        if (state.message.contains("No internet connection")) {
                                binding?.root?.findViewById<View>(R.id.itemError)?.isVisible  = true

                        } else {
                            binding?.loadingScreen?.visibility = View.INVISIBLE
                            binding?.weatherDataLayout?.visibility = View.VISIBLE
                            binding?.root?.findViewById<View>(R.id.itemError)?.isVisible  = false
                            showToast("Error: ${state.message}")
                        }
                       // showToast(state.message)

                    }

                    is WeatherState.FavoriteStatus -> {
                        isFavorite = state.isFavorite
                        updateFabIcon(isFavorite)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun updateWeatherUI(state: WeatherState.Success) {
        binding?.apply {
            tvDate.text = state.weather.name
            tempText.text = "${state.weather.main.temp.roundToInt()}°C"
            weatherStatusText.text = state.weather.weather.firstOrNull()?.main ?: "N/A"
            feelsLikeText.text = "${state.weather.main.feels_like.roundToInt()}°C"
            highTempText.text = "${state.weather.main.temp_max.roundToInt()}°C"
            lowTempText.text = "${state.weather.main.temp_min.roundToInt()}°C"
            pressureText.text = "${state.weather.wind.speed.roundToInt()} Km/h"
            humidityText.text = "${state.weather.main.humidity}%"

            val iconRes = getWeatherIcon(state.weather.weather.firstOrNull()?.main ?: "")
            ivMainWeatherIcon.setImageResource(iconRes)
        }

        weatherViewModel.onEvent(WeatherEvent.CheckFavoriteStatus(state.weather.name))
        updateFabIcon(isFavorite)
    }

    private fun setupFabListener(weather: weather) {
        binding?.fab?.setOnClickListener {
            if (!isFavorite) {
                weatherViewModel.onEvent(WeatherEvent.SaveWeather(weather))
                isFavorite = true
                updateFabIcon(true)
                showSnackbar("Added to Favorites")
            } else {
                showSnackbar("Already in Favorites")
            }
        }
    }

    private fun checkLocationPermissionAndFetchWeather() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getDeviceLocation()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showToast("Location permission not granted")
                return
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    getCityFromCoordinates(location.latitude, location.longitude)
                } else {
                    showToast("Unable to get location")
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            showToast("Security exception: ${e.message}")
        }
    }

    private fun getCityFromCoordinates(lat: Double, lon: Double) {
        lifecycleScope.launch {
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val cityName = addresses?.firstOrNull()?.locality

                if (!cityName.isNullOrEmpty()) {
                    lastFetchedCityName = cityName
                    weatherViewModel.onEvent(WeatherEvent.FetchWeather(cityName))
                } else {
                    showToast("City not found from location")
                }
            } catch (e: Exception) {
                showToast("Error getting city: ${e.message}")
            }
        }
    }

    private fun updateFabIcon(isFavorite: Boolean) {
        binding?.fab?.setImageResource(
            if (isFavorite) R.drawable.baseline_ffavorite_24
            else R.drawable.baseline_favorite_24
        )
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}

private fun getWeatherIcon(main: String): Int {
    return when (main.lowercase()) {
        "clear" -> R.drawable.clear
        "clouds" -> R.drawable.clouds_pre
        "rain" -> R.drawable.rain
        "drizzle" -> R.drawable.drizzle
        "thunderstorm" -> R.drawable.clouds_moon
        "snow" -> R.drawable.snow
        "mist", "fog" -> R.drawable.mist
        "smoke" -> R.drawable.mist
        "haze" -> R.drawable.mist
        "dust", "sand", "ash" -> R.drawable.clouds_pre
        "squall", "tornado" -> R.drawable.wind
        else -> R.drawable.clouds // default icon
    }

}


