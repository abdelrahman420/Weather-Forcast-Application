package com.example.climatic.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climatic.model.repository.Repository
import com.example.climatic.settings_screen.viewmodel.SettingsViewModel

class HomeViewModelFactory(
    private val repository: Repository,
    private val settingsViewModel: SettingsViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository,settingsViewModel) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}


