package com.example.climatic.model.repository

import com.example.climatic.model.database.LocalDataSource
import com.example.climatic.model.network.RemoteDataSource
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {


    companion object {
        @Volatile
        private var instance: RepositoryImpl? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): RepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: RepositoryImpl(
                    remoteDataSource,
                    localDataSource
                ).also { instance = it }
            }
        }
    }


    override suspend fun _getCurrentWeatherByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Flow<WeatherResponse> = flow {
        val response = remoteDataSource.getCurrentWeatherByCity(city, apiKey, units, language)
        if (response.isSuccessful) {
            val weatherResponse = response.body()
            weatherResponse?.let {
                emit(it)
            } ?: throw Exception("Weather data is null")
        } else
            throw Exception("Failed to fetch weather data")
    }

    override suspend fun _getCurrentWeatherByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<WeatherResponse> = flow {
        val response = remoteDataSource.getCurrentWeatherByLatLon(lat, lon, apiKey, units, language)
        if (response.isSuccessful) {
            val weatherResponse = response.body()
            weatherResponse?.let {
                emit(it)
            } ?: throw Exception("Weather data is null")
        } else
            throw Exception("Failed to fetch weather data")
    }

    override suspend fun _getWeatherForecastByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Flow<ForecastResponse> = flow {
        val response = remoteDataSource.getWeatherForecastByCity(city, apiKey, units, language)
        if (response.isSuccessful) {
            val forecastResponse = response.body()
            forecastResponse?.let {
                emit(it)
            } ?: throw Exception("Forecast data is null")
        } else {
            throw Exception("Failed to fetch weather forecast: ${response.message()}")
        }
    }

    override suspend fun _getWeatherForecastByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<ForecastResponse> =  flow {
        val response = remoteDataSource.getWeatherForecastByLatLon(lat, lon, apiKey, units, language)
        if (response.isSuccessful) {
            val forecastResponse = response.body()
            forecastResponse?.let {
                emit(it)
            } ?: throw Exception("Forecast data is null")
        } else {
            throw Exception("Failed to fetch weather forecast: ${response.message()}")
        }
    }
    override suspend fun _getAllLocations(): Flow<List<ForecastResponse>> {
        return localDataSource.getAllLocations()
    }

    override suspend fun _insertLocation(location: ForecastResponse): Long {
        return localDataSource.insertLocation(location)
    }

    override suspend fun _insertLocations(locations: List<ForecastResponse>): List<Long> {
        return localDataSource.insertLocations(locations)
    }

    override suspend fun _getLocationsCount(): Int {
        return localDataSource.getLocationsCount()
    }

    override suspend fun _updateLocation(location: ForecastResponse) {
        return localDataSource.updateLocation(location)
    }

    override suspend fun _deleteLocation(location: ForecastResponse): Int {
        return localDataSource.deleteLocation(location)
    }
}