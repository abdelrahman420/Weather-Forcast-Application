package com.example.climatic.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatic.model.ForecastState
import com.example.climatic.model.WeatherState
import com.example.climatic.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val _forecastState = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val forecastState: StateFlow<ForecastState> = _forecastState

    fun getWeatherbyLatLon(lat: Double, lon: Double, apiKey: String = "74eea8bc50e4e92c23d95d31ee9f948e", units: String = "metric") {
        viewModelScope.launch(Dispatchers.IO) {
            repository._getCurrentWeatherByLatLon(lat, lon, apiKey, units)
                .catch{ exception ->
                    _weatherState.value = WeatherState.Error("Error: ${exception.message}")
                }
                .collect { weather ->
                    if (weather != null) {
                        _weatherState.value = WeatherState.Success(weather)
                    } else {
                        _weatherState.value = WeatherState.Error("No data available.")
                    }
                }
        }
    }

    fun getWeatherbyCity(city: String, apiKey: String = "74eea8bc50e4e92c23d95d31ee9f948e", units: String = "metric") {
        viewModelScope.launch(Dispatchers.IO) {
            repository._getCurrentWeatherByCity(city, apiKey, units)
                .catch{ exception ->
                    _weatherState.value = WeatherState.Error("Error: ${exception.message}")
                }
                .collect { weather ->
                    if (weather != null) {
                        _weatherState.value = WeatherState.Success(weather)
                    } else {
                        _weatherState.value = WeatherState.Error("No data available.")
                    }
                }
        }
    }
    fun getForecastByLatLon(lat: Double, lon: Double, apiKey: String = "74eea8bc50e4e92c23d95d31ee9f948e", units: String = "metric") {
        viewModelScope.launch(Dispatchers.IO) {
            repository._getWeatherForecastByLatLon(lat, lon, apiKey, units)
                .catch{ exception ->
                    _forecastState.value = ForecastState.Error("Error: ${exception.message}")
                }
                .collect { forecast ->
                    if (forecast != null) {
                        _forecastState.value = ForecastState.Success(forecast)
                    } else {
                        _forecastState.value = ForecastState.Error("No data available.")
                    }
                }
        }
    }

    fun getForecastByCity(city: String, apiKey: String = "74eea8bc50e4e92c23d95d31ee9f948e", units: String = "metric") {
        viewModelScope.launch(Dispatchers.IO) {
            repository._getWeatherForecastByCity(city, apiKey, units)
                .catch{ exception ->
                    _forecastState.value = ForecastState.Error("Error: ${exception.message}")
                }
                .collect { forecast ->
                    if (forecast != null) {
                        _forecastState.value = ForecastState.Success(forecast)
                    } else {
                        _forecastState.value = ForecastState.Error("No data available.")
                    }
                }
        }
    }
}