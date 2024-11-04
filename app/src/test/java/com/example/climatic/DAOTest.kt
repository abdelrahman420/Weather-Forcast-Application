package com.example.climatic

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.climatic.model.database.DAO
import com.example.climatic.model.database.WeatherDB
import com.example.climatic.model.dtos.Favourites
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DAOTest {

    private lateinit var database: WeatherDB
    private lateinit var dao: DAO

    @Before
    fun setup() {
        // Set up an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDB::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertFavLocation_insertsLocationSuccessfully() = runBlocking {
        val location = Favourites(lat = 40.7128, lon = -74.0060) // Coordinates for New York
        dao.insertFavLocation(location)

        val result = dao.allFavLocations().first() // Collect the first emitted value
        assertEquals(1, result.size)
        assertEquals(40.7128, result[0].lat)
        assertEquals(-74.0060, result[0].lon)
    }

    @Test
    fun deleteFavLocation_deletesLocationSuccessfully() = runBlocking {
        val location = Favourites(lat = 40.7128, lon = -74.0060)
        dao.insertFavLocation(location) // Insert first
        dao.deleteFavLocation(location) // Then delete

        val result = dao.allFavLocations().first()
        assertEquals(0, result.size) // Verify that no locations remain
    }
}