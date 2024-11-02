package com.example.climatic.model.database

import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.responses.ForecastResponse
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val dao: DAO) : LocalDataSource {

    companion object {
        @Volatile
        private var instance: LocalDataSourceImpl? = null

        fun getInstance(dao: DAO): LocalDataSourceImpl {
            return instance ?: synchronized(this) {
                instance ?: LocalDataSourceImpl(dao).also { instance = it }
            }
        }
    }

    override suspend fun getAllFavLocations(): Flow<List<Favourites>> {
        return dao.allFavLocations()
    }

    override suspend fun insertFavLocation(location: Favourites): Long {
        return dao.insertFavLocation(location)
    }

    override suspend fun insertLocations(locations: List<Favourites>): List<Long> {
        return dao.insertLocations(locations)
    }

    override suspend fun deleteFavLocation(location: Favourites): Int {
        return dao.deleteFavLocation(location)
    }

    override suspend fun getLocationsCount(): Int {
        return dao.getLocationsCount()
    }

    override suspend fun updateLocation(location: ForecastResponse) {
        return dao.updateLocation(location)
    }

}