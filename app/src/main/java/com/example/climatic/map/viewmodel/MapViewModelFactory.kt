package com.example.climatic.map.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climatic.model.network.NominatimApiService

class MapViewModelFactory(private val nominatimService: NominatimApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(nominatimService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
