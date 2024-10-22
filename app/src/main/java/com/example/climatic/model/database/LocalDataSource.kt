package com.example.climatic.model.database

import com.example.climatic.model.responses.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllLocations(): Flow<List<ForecastResponse>>
    suspend fun insertLocation(location: ForecastResponse): Long
    suspend fun insertLocations(locations: List<ForecastResponse>): List<Long>
    suspend fun getLocationsCount(): Int
    suspend fun updateLocation(location: ForecastResponse)
    suspend fun deleteLocation(location: ForecastResponse): Int
}