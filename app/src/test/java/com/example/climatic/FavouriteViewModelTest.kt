package com.example.climatic

import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.repository.FakeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouriteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var fakeRepository: FakeRepository
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeRepository()
        viewModel = FavouriteViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

//    @Before
//    fun setup() {
//        fakeRepository = FakeRepository()
//        viewModel = FavouriteViewModel(fakeRepository)
//    }

    @Test
    fun `addFavorite adds location to favorites`() = runTest {
        // Arrange
        val mockLocation = Favourites(lat = 40.7128, lon = -74.0060)

        // Act
        viewModel.addFavorite(mockLocation)

        // Assert
        val favoriteLocations = viewModel.forecastState.first()
        assertTrue(favoriteLocations.contains(mockLocation))
    }

    @Test
    fun `removeFavorite removes location from favorites`() = runTest {
        // Arrange
        val mockLocation = Favourites(lat = 40.7128, lon = -74.0060)
        viewModel.addFavorite(mockLocation)

        // Act
        viewModel.removeFavorite(mockLocation)

        // Assert
        val favoriteLocations = viewModel.forecastState.first()
        assertTrue(!favoriteLocations.contains(mockLocation))
    }
}
