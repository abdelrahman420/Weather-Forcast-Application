package com.example.climatic.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("SELECT * FROM favourites")
    fun allFavLocations(): Flow<List<Favourites>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavLocation(location: Favourites): Long

    @Delete
    fun deleteFavLocation(location: Favourites): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(locations: List<Favourites>): List<Long>

    @Query("SELECT COUNT(*) FROM forecast_table")
    suspend fun getLocationsCount(): Int

    @Update
    suspend fun updateLocation(location: ForecastResponse)

}