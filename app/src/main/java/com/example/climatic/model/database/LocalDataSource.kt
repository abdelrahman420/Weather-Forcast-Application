package com.example.climatic.model.database

import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.responses.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllFavLocations(): Flow<List<Favourites>>
    suspend fun insertFavLocation(location: Favourites): Long
    suspend fun insertLocations(locations: List<Favourites>): List<Long>
    suspend fun deleteFavLocation(location: Favourites): Int
    suspend fun getLocationsCount(): Int
    suspend fun updateLocation(location: ForecastResponse)
}