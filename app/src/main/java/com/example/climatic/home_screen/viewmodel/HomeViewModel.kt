package com.example.climatic.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatic.model.State
import com.example.climatic.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _forecastState = MutableStateFlow<State>(State.Loading)
    val forecastState: StateFlow<State> = _forecastState

    fun getWeatherbyLatLon(lat: Double, lon: Double, apiKey: String = "74eea8bc50e4e92c23d95d31ee9f948e", units: String = "metric") {
        viewModelScope.launch(Dispatchers.IO) {
            repository._getCurrentWeatherByLatLon(lat, lon, apiKey, units)
                .catch{ exception ->
                    _forecastState.value = State.Error("Error: ${exception.message}")
                }
                .collect { products ->
                    if (products != null) {
                        _forecastState.value = State.Success(products)
                    } else {
                        _forecastState.value = State.Error("No data available.")
                    }
                }
        }
    }
}