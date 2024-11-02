package com.example.climatic.favourites_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatic.model.FavoriteState
import com.example.climatic.model.ForecastState
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.repository.Repository
import com.example.climatic.model.responses.ForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: Repository) : ViewModel() {

    private val _forecastState = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val forecastState: StateFlow<FavoriteState> = _forecastState

//    init {
//        getFavoriteLocations()
//    }

    fun addFavorite(location: Favourites) {
        viewModelScope.launch {
            repository._insertFavLocation(location)
        }
    }

    fun removeFavorite(location: Favourites) {
        viewModelScope.launch {
            repository._deleteFavLocation(location)
        }

    }

//    fun getFavoriteLocations() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _forecastState.value = FavoriteState.Loading
//            repository._getAllFavLocations().catch { exception ->
//                _forecastState.value = FavoriteState.Error("Error: ${exception.message}")
//            }.collect { locations ->
//                if (locations.isNotEmpty()) {
//                    _forecastState.value = FavoriteState.Success(locations)
//                } else {
//                    _forecastState.value = FavoriteState.Error("No products available")
//                }
//            }
//        }
//    }
}

