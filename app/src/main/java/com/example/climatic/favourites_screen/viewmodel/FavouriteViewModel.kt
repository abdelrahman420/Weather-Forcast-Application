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

    private val _forecastState = MutableStateFlow<List<Favourites>>(emptyList())
    val forecastState: StateFlow<List<Favourites>> = _forecastState

    init {
        getFavoriteLocations()
    }

    fun addFavorite(location: Favourites) {
        viewModelScope.launch {
            repository._insertFavLocation(location)
        }
    }

    fun removeFavorite(location: Favourites) {
        viewModelScope.launch(Dispatchers.IO) {
            repository._deleteFavLocation(location)
        }

    }

    fun getFavoriteLocations() {
        viewModelScope.launch {
            repository._getAllFavLocations().collect {
                _forecastState.value = it
            }
        }
    }
}

