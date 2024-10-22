package com.example.climatic.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("SELECT * FROM locations_table")
    fun allLocations(): Flow<List<ForecastResponse>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: ForecastResponse): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(locations: List<ForecastResponse>): List<Long>

    @Query("SELECT COUNT(*) FROM locations_table")
    suspend fun getLocationsCount(): Int

    @Update
    suspend fun updateLocation(location: ForecastResponse)

    @Delete
    fun deleteLocation(location: ForecastResponse) : Int
}