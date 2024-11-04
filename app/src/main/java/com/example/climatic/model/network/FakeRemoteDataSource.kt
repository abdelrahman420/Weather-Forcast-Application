package com.example.climatic.model.network


import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import retrofit2.Response

class FakeRemoteDataSource : RemoteDataSource {

    var weatherResponse: WeatherResponse? = null
    var forecastResponse: ForecastResponse? = null
    var isSuccessful: Boolean = true

    override suspend fun getCurrentWeatherByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Response<WeatherResponse> {
        return if (isSuccessful) {
            Response.success(weatherResponse)
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "Error"))
        }
    }

    override suspend fun getCurrentWeatherByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Response<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherForecastByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Response<ForecastResponse> {
        return if (isSuccessful) {
            Response.success(forecastResponse)
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "Error"))
        }
    }

    override suspend fun getWeatherForecastByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Response<ForecastResponse> {
        TODO("Not yet implemented")
    }

}
