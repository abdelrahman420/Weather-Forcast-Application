package com.example.climatic.settings_screen.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedLanguage = MutableLiveData<String>()
    val selectedLanguage: LiveData<String> get() = _selectedLanguage

    private val _selectedTemperatureUnit = MutableLiveData<String>()
    val selectedTemperatureUnit: LiveData<String> get() = _selectedTemperatureUnit

    private val _selectedWindSpeedUnit = MutableLiveData<String>()
    val selectedWindSpeedUnit: LiveData<String> get() = _selectedWindSpeedUnit

    private val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    init {
        _selectedLanguage.value = sharedPreferences.getString("language", "en")
        _selectedTemperatureUnit.value = sharedPreferences.getString("temperature_unit", "metric")
        _selectedWindSpeedUnit.value = sharedPreferences.getString("wind_speed_unit", "metric")
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
        saveToPreferences("language", language)
    }

    fun setTemperatureUnit(unit: String) {
        _selectedTemperatureUnit.value = unit
        saveToPreferences("temperature_unit", unit)
    }

    fun setWindSpeedUnit(unit: String) {
        _selectedWindSpeedUnit.value = unit
        saveToPreferences("wind_speed_unit", unit)
    }

    private fun saveToPreferences(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }
}
