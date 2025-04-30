package com.example.hazelweather.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hazelweather.databinding.FragmentFavouriteBinding
import com.example.hazelweather.ui.adapters.WeatherAdapter
import com.example.hazelweather.ui.events.WeatherEvent
import com.example.hazelweather.ui.state.WeatherState
import com.example.hazelweather.ui.viewmodel.WeatherViewModel
import com.example.hazelweather.ui.viewmodel.WeatherViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.hazelmobile.cores.bases.fragment.BaseFragment
import kotlinx.coroutines.launch


class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(FragmentFavouriteBinding::inflate) {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    override fun FragmentFavouriteBinding.bindViews() {
        setupRecyclerView()
    }

    override fun FragmentFavouriteBinding.bindListeners() {
        setUpSwipeToDelete()
    }

    override fun FragmentFavouriteBinding.bindObservers() {
        observeWeatherState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val factory = WeatherViewModelFactory(requireContext())
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        // Trigger fetching favorites
        weatherViewModel.getFavoriteWeather()
    }

    private fun setupRecyclerView() {
        weatherAdapter = WeatherAdapter()
        binding?.recyclerFavourites?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weatherAdapter
        }
    }

    private fun observeWeatherState() {
        viewLifecycleOwner.lifecycleScope.launch {
            weatherViewModel.state.collect { state ->
                when (state) {
                    is WeatherState.Loading -> {
                        binding?.emptyStateTextView?.visibility = View.GONE
                    }

                    is WeatherState.WeatherListState -> {
                        binding?.emptyStateTextView?.visibility =
                            if (state.weatherList.isEmpty()) View.VISIBLE else View.GONE

                        weatherAdapter.submitList(state.weatherList.toList())
                    }

                    is WeatherState.Error -> {
                        binding?.emptyStateTextView?.text = state.message
                        binding?.emptyStateTextView?.visibility = View.VISIBLE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setUpSwipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val weather = weatherAdapter.currentList.getOrNull(viewHolder.adapterPosition)
                weather?.let {
                    weatherViewModel.onEvent(WeatherEvent.DeleteWeather(it))

                    Snackbar.make(viewHolder.itemView, "Removed from favorites", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            weatherViewModel.onEvent(WeatherEvent.SaveWeather(weather))
                            weatherViewModel.getFavoriteWeather()
                        }
                        .show()

                    weatherViewModel.getFavoriteWeather()
                }
            }
        }

        binding?.let {
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(it.recyclerFavourites)
        }
    }

}



