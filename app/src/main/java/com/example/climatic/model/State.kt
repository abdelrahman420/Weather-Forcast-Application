package com.example.climatic.model

import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

sealed class ForecastState {
    object Loading : ForecastState()
    data class Success(val forecast: ForecastResponse) : ForecastState()
    data class Error(val message: String) : ForecastState()
}

sealed class FavoriteState {
    object Loading : FavoriteState()
    data class Success(val favorite: List<Favourites>) : FavoriteState()
    data class Error(val message: String) : FavoriteState()
}