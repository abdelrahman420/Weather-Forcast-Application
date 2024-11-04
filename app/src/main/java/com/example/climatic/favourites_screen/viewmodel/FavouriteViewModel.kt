package com.example.climatic.favourites_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: Repository) : ViewModel() {

    private val _forecastState = MutableStateFlow<List<Favourites>>(emptyList())
    val forecastState: StateFlow<List<Favourites>> = _forecastState

    init {
        getFavoriteLocations()
    }

    fun addFavorite(location: Favourites) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository._insertFavLocation(location)
                getFavoriteLocations()
            } catch (e: Exception) {
            }
        }
    }

    fun removeFavorite(location: Favourites) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository._deleteFavLocation(location)
                getFavoriteLocations()
            } catch (e: Exception) {

            }
        }
    }

    private fun getFavoriteLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            repository._getAllFavLocations().collect { favorites ->
                _forecastState.value = favorites
            }
        }
    }
}
