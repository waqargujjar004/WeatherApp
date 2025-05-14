package com.example.hazelweather.ui.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hazelweather.R
import com.example.hazelweather.databinding.FragmentSearchBinding
import com.example.hazelweather.domain.models.weather
import com.example.hazelweather.ui.events.WeatherEvent
import com.example.hazelweather.ui.state.WeatherState
import com.example.hazelweather.ui.viewmodel.WeatherViewModel
import com.example.hazelweather.ui.viewmodel.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.hazelmobile.cores.bases.fragment.BaseFragment
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private lateinit var weatherViewModel: WeatherViewModel
    private var isFavorite: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var lastFetchedCityName: String = ""

    override fun FragmentSearchBinding.bindViews() {
        initViewModel()
        setupLocationClient()
        checkLocationPermissionAndFetchWeather()
    }

    override fun FragmentSearchBinding.bindListeners() {

        setupSearchListener()
        SetNetwork()

    }

    override fun FragmentSearchBinding.bindObservers() {
        observeWeatherState()
    }
    private fun SetNetwork() {
        binding?.itemError?.retryButton?.setOnClickListener {
            weatherViewModel.onEvent(WeatherEvent.FetchWeather(lastFetchedCityName))
        }
    }
    private fun initViewModel() {
        val factory = WeatherViewModelFactory(requireContext())
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun setupSearchListener() {
        FirebaseCrashlytics.getInstance().log("Search button clicked with city")

        binding?.editText2?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                lifecycleScope.launch {
                    val query = binding?.editText2?.text.toString().trim()
                    if (query.isNotEmpty()) {
                        weatherViewModel.onEvent(WeatherEvent.FetchWeather(query))
                        hideKeyboard()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a correct city",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true

            } else {
                false
            }
        }
    }

    private fun observeWeatherState() {
        viewLifecycleOwner.lifecycleScope.launch {
            weatherViewModel.state.collect { state ->
                when (state) {
                    is WeatherState.Loading -> {
                        binding?.weatherDataLayout?.visibility = View.GONE
                        binding?.loadingScreen?.visibility = View.VISIBLE
                    }

                    is WeatherState.Success -> {
                        binding?.root?.findViewById<View>(R.id.itemError)?.isVisible  = false
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
                           // showToast("Error: ${state.message}")
                            showToast(state.message + " Please enter correct city")
                        }

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
                    weatherViewModel.onEvent(WeatherEvent.FetchWeather(cityName))
                    lastFetchedCityName = cityName
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
    private fun hideKeyboard() {
        binding?.editText2?.let { editText ->
            editText.clearFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        }
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
