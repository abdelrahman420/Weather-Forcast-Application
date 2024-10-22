package com.example.climatic.model

import com.example.climatic.model.responses.WeatherResponse

sealed class State {
    object Loading : State()
    data class Success(val weather: WeatherResponse) : State()
    data class Error(val message: String) : State()
}