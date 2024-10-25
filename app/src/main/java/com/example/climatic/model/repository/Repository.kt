package com.example.climatic.model.repository

import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    suspend fun _getCurrentWeatherByCity(city: String, apiKey: String, units: String = "metric", language: String = "en"): Flow<WeatherResponse>
    suspend fun _getCurrentWeatherByLatLon(lat: Double, lon: Double, apiKey: String, units: String = "metric", language: String = "en"): Flow<WeatherResponse>
    suspend fun _getWeatherForecastByCity(city: String, apiKey: String, units: String = "metric", language: String = "en"): Flow<ForecastResponse>
    suspend fun _getWeatherForecastByLatLon(lat: Double, lon: Double, apiKey: String, units: String = "metric", language: String = "en"): Flow<ForecastResponse>
    suspend fun _getAllLocations(): Flow<List<ForecastResponse>>
    suspend fun _insertLocation(location: ForecastResponse): Long
    suspend fun _insertLocations(locations: List<ForecastResponse>): List<Long>
    suspend fun _getLocationsCount(): Int
    suspend fun _updateLocation(location: ForecastResponse)
    suspend fun _deleteLocation(location: ForecastResponse): Int
}

