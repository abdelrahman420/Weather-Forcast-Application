package com.example.climatic.model.network

import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import retrofit2.Response

class RemoteDataSourceImpl : RemoteDataSource {

    private val retrofitService = RetrofitClient.service

    companion object {
        @Volatile
        private var instance: RemoteDataSourceImpl? = null
        fun getInstance(): RemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                instance ?: RemoteDataSourceImpl().also { instance = it }
            }
        }
    }

    override suspend fun getCurrentWeatherByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Response<WeatherResponse> {
        return retrofitService.getCurrentWeatherbyCity(city, apiKey, units, language)
    }

    override suspend fun getCurrentWeatherByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Response<WeatherResponse> {
        return retrofitService.getCurrentWeatherByLatLon(lat, lon, apiKey, units, language)
    }

    override suspend fun getWeatherForecastByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Response<ForecastResponse> {
        return retrofitService.getWeatherForecastByCity(city, apiKey, units, language)
    }

    override suspend fun getWeatherForecastByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Response<ForecastResponse> {
        return retrofitService.getWeatherForecastByLatLon(lat, lon, apiKey, units, language)
    }
}