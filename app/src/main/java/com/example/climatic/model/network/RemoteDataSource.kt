package com.example.climatic.model.network


import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getCurrentWeatherByCity(city: String, apiKey: String, units: String = "metric"): Response<WeatherResponse>
    suspend fun getCurrentWeatherByLatLon(lat: Double, lon: Double, apiKey: String, units: String = "metric"): Response<WeatherResponse>
    suspend fun getWeatherForecastByCity(city: String, apiKey: String, units: String = "metric"): Response<ForecastResponse>
    suspend fun getWeatherForecastByLatLon(lat: Double, lon: Double, apiKey: String, units: String = "metric"): Response<ForecastResponse>
}
