package com.example.climatic.model.repository

import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : Repository {

    private val favLocations = mutableListOf<Favourites>()
    override suspend fun _getCurrentWeatherByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun _getCurrentWeatherByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun _getWeatherForecastByCity(
        city: String,
        apiKey: String,
        units: String,
        language: String
    ): Flow<ForecastResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun _getWeatherForecastByLatLon(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<ForecastResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun _getAllFavLocations(): Flow<List<Favourites>> {
        return flow { emit(favLocations) }
    }

    override suspend fun _insertFavLocation(location: Favourites): Long {
        favLocations.add(location)
        return 1L // Simulate successful insertion
    }

    override suspend fun _insertLocations(locations: List<Favourites>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun _deleteFavLocation(location: Favourites): Int {
        favLocations.remove(location)
        return 1 // Simulate successful deletion
    }

    override suspend fun _getLocationsCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun _updateLocation(location: ForecastResponse) {
        TODO("Not yet implemented")
    }

    // Implement other methods as needed (returning dummy values or stubs)
}
