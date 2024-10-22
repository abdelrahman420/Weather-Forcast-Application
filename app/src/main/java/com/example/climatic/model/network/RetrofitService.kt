package com.example.climatic.model.network

import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherbyCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByLatLon(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecastByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<ForecastResponse>

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecastByLatLon(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<ForecastResponse>


}
