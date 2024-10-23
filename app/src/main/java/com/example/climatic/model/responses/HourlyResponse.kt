package com.example.climatic.model.responses

import com.example.climatic.model.dtos.Main
import com.example.climatic.model.dtos.Weather


data class HourlyResponse(
    val list: List<HourlyForecast>
)

data class HourlyForecast(
    val dt_txt: String? = null,
    val temp: Double? = null,
    val icon: String? = null
)


