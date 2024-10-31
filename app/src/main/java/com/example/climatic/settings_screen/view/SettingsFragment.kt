package com.example.climatic.settings_screen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.climatic.R
import com.example.climatic.settings_screen.viewmodel.SettingsViewModel
import java.util.Locale


class SettingsFragment : Fragment() {
    private lateinit var settingsViewModel: SettingsViewModel
    var flag: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingsViewModel::class.java)
        val radioGroup = view.findViewById<RadioGroup>(R.id.language_group)
        val radioArabic = view.findViewById<RadioButton>(R.id.language_arabic)
        val radioEnglish = view.findViewById<RadioButton>(R.id.language_english)

       // settingsViewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)
        settingsViewModel.selectedLanguage.observe(viewLifecycleOwner) { language ->
            when (language) {
                "ar" -> radioGroup.check(radioArabic.id)
                "en" -> radioGroup.check(radioEnglish.id)
            }
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLanguage = when (checkedId) {
                R.id.language_arabic -> "ar"
                R.id.language_english -> "en"
                else -> "en"
            }
            settingsViewModel.setLanguage(selectedLanguage)
        }


        val temperatureUnitGroup = view.findViewById<RadioGroup>(R.id.temperature_unit_group)
        val radioCelsius = view.findViewById<RadioButton>(R.id.temperature_celsius)
        val radioKelvin = view.findViewById<RadioButton>(R.id.temperature_kelvin)
        val radioFahrenheit = view.findViewById<RadioButton>(R.id.temperature_fahrenheit)

        // Observe selected temperature unit
        settingsViewModel.selectedTemperatureUnit.observe(viewLifecycleOwner) { unit ->
            when (unit) {
                "standard" -> temperatureUnitGroup.check(radioKelvin.id)
                "imperial" -> temperatureUnitGroup.check(radioFahrenheit.id)
                "metric" -> temperatureUnitGroup.check(radioCelsius.id)
            }
        }
        temperatureUnitGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.temperature_kelvin -> "standard"
                R.id.temperature_fahrenheit -> "imperial"
                R.id.temperature_celsius -> "metric"
                else -> "metric"
            }
            settingsViewModel.setTemperatureUnit(selectedUnit)
        }

        val windSpeedUnitGroup = view.findViewById<RadioGroup>(R.id.wind_speed_unit_group)
        val radioMPS = view.findViewById<RadioButton>(R.id.speed_mps)
        val radioMPH = view.findViewById<RadioButton>(R.id.speed_mph)
        settingsViewModel.selectedWindSpeedUnit.observe(viewLifecycleOwner) { unit ->
            when (unit) {
                "metric" -> windSpeedUnitGroup.check(radioMPS.id)
                "imperial" -> windSpeedUnitGroup.check(radioMPH.id)
            }
        }
        windSpeedUnitGroup.setOnCheckedChangeListener{ _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.speed_mph -> "imperial"
                R.id.speed_mps -> "metric"
                else -> "metric"
            }
            settingsViewModel.setWindSpeedUnit(selectedUnit)
        }
    }
}