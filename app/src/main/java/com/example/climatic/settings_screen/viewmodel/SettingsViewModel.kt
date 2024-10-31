package com.example.climatic.settings_screen.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.climatic.main_screen.view.MainActivity
import com.example.climatic.utils.LanguageManager
import java.util.Locale

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    val selectedLanguage = MutableLiveData<String>()
    val selectedTemperatureUnit = MutableLiveData<String>()
    val selectedWindSpeedUnit = MutableLiveData<String>()
    init {
        selectedLanguage.value = sharedPreferences.getString("language", "en")
        selectedTemperatureUnit.value = sharedPreferences.getString("temperature_unit", "metric")
        selectedWindSpeedUnit.value = sharedPreferences.getString("wind_speed_unit", "metric")
    }
    fun setLanguage(language: String) {
        selectedLanguage.value = language
        editor.putString("language", language).apply()
        LanguageManager.updateLanguage(getApplication(), language)
    }
    fun setTemperatureUnit(unit: String) {
        selectedTemperatureUnit.value = unit
        editor.putString("temperature_unit", unit).apply()
    }
    fun setWindSpeedUnit(unit: String) {
        selectedWindSpeedUnit.value = unit
        editor.putString("wind_speed_unit", unit).apply()
    }
}
