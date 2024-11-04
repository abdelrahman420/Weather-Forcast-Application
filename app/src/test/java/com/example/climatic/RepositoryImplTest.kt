package com.example.climatic
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.network.FakeRemoteDataSource
import com.example.climatic.model.database.FakeLocalDataSource
import com.example.climatic.model.dtos.Clouds
import com.example.climatic.model.dtos.Coord
import com.example.climatic.model.dtos.Main
import com.example.climatic.model.dtos.Sys
import com.example.climatic.model.dtos.Weather
import com.example.climatic.model.dtos.Wind
import com.example.climatic.model.repository.RepositoryImpl
import com.example.climatic.model.responses.WeatherResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RepositoryImplTest {

    private lateinit var repository: RepositoryImpl
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeLocalDataSource: FakeLocalDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeRemoteDataSource()
        fakeLocalDataSource = FakeLocalDataSource()
        repository = RepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun getCurrentWeatherByCity_returns_correct_weather_response() = runBlocking {
        // Arrange
        val mockWeatherResponse = WeatherResponse(
            coord = Coord(lon = -0.1257, lat = 51.5085),
            weather = listOf(Weather(id = 804, main = "Clouds", description = "overcast clouds", icon = "04n")),
            base = "stations",
            main = Main(temp = 284.0, feelsLike = 283.36, tempMin = 283.01, tempMax = 284.82, pressure = 1027, humidity = 85, seaLevel = 1027, grndLevel = 1022),
            visibility = 10000,
            wind = Wind(speed = 2.57, deg = 110),
            clouds = Clouds(all = 100),
            dt = 1730680982,
            sys = Sys(type = 2, id = 2075535, country = "GB", sunrise = 1730703590, sunset = 1730737697),
            timezone = 0,
            id = 2643743,
            name = "London",
            cod = 200
        )

        fakeRemoteDataSource.weatherResponse = mockWeatherResponse
        fakeRemoteDataSource.isSuccessful = true

        // Act
        val result = repository._getCurrentWeatherByCity("London", "testApiKey").first()

        // Assert
        assertEquals(mockWeatherResponse, result)
    }

    @Test
    fun insertFavLocation_should_add_location_to_local_data_source() = runBlocking {
        // Arrange
        val mockLocation = Favourites(lat = 40.7128, lon = -74.0060)

        // Act
        val id = repository._insertFavLocation(mockLocation)

        // Assert
        val favLocations = fakeLocalDataSource.getAllFavLocations().first()
        assertTrue { favLocations.contains(mockLocation) }
        assertEquals(1L, id)
    }
}
