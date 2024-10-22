package com.example.climatic.home_screen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.climatic.R
import com.example.climatic.home_screen.viewmodel.HomeViewModel
import com.example.climatic.home_screen.viewmodel.HomeViewModelFactory
import com.example.climatic.model.State
import com.example.climatic.model.database.LocalDataSourceImpl
import com.example.climatic.model.database.WeatherDB
import com.example.climatic.model.network.RemoteDataSourceImpl
import com.example.climatic.model.repository.RepositoryImpl
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel with Repository
        homeViewModelFactory = HomeViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(requireContext()).dao())
            )
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        lifecycleScope.launch {
            homeViewModel.forecastState.collect { state ->
                when (state) {
                    is State.Loading -> {
                        Log.d("HomeFragment", "Loading data...")
                    }
                    is State.Success -> {
                        val weatherData = state.weather
                        Log.d("HomeFragment", "Weather Data: $weatherData")
                    }
                    is State.Error -> {
                        Log.e("HomeFragment", "Error: ${state.message}")
                    }
                }
            }
        }
        homeViewModel.getWeatherbyLatLon(lat = 40.7128, lon = -74.0060)
    }
}
