package com.example.climatic.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.responses.ForecastResponse

@Database(entities = arrayOf(ForecastResponse::class, Favourites::class), version = 2)
@TypeConverters(Converters::class)
abstract class WeatherDB : RoomDatabase() {
    abstract fun dao(): DAO
    companion object {
        private var INSTANCE: WeatherDB? = null
        fun getInstance(ctx: Context): WeatherDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    WeatherDB::class.java,
                    "locations_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}