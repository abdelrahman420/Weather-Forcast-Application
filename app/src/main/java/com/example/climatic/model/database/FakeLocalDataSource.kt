package com.example.climatic.model.database

import com.example.climatic.model.responses.ForecastResponse

import com.example.climatic.model.dtos.Favourites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource : LocalDataSource {

    private val favLocations = mutableListOf<Favourites>()

    override suspend fun getAllFavLocations(): Flow<List<Favourites>> {
        return flow { emit(favLocations) }
    }

    override suspend fun insertFavLocation(location: Favourites): Long {
        favLocations.add(location)
        return 1L // Simulate successful insertion
    }

    override suspend fun insertLocations(locations: List<Favourites>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavLocation(location: Favourites): Int {
        favLocations.remove(location)
        return 1 // Simulate successful deletion
    }

    override suspend fun getLocationsCount(): Int {
        return favLocations.size
    }

    override suspend fun updateLocation(location: ForecastResponse) {
        TODO("Not yet implemented")
    }

}
