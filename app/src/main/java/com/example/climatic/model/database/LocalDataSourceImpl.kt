package com.example.climatic.model.database

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

    override suspend fun getAllLocations(): Flow<List<ForecastResponse>> {
        return dao.allLocations()
    }

    override suspend fun insertLocation(location: ForecastResponse): Long {
        return dao.insertLocation(location)
    }

    override suspend fun insertLocations(locations: List<ForecastResponse>): List<Long> {
        return dao.insertLocations(locations)
    }

    override suspend fun getLocationsCount(): Int {
        return dao.getLocationsCount()
    }

    override suspend fun updateLocation(location: ForecastResponse) {
        return dao.updateLocation(location)
    }

    override suspend fun deleteLocation(location: ForecastResponse): Int {
        return dao.deleteLocation(location)
    }
}