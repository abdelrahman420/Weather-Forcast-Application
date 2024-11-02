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
import com.example.climatic.utils.LanguageUtils


class SettingsFragment : Fragment() {
    private lateinit var languageGroup: RadioGroup
    private lateinit var radioArabic : RadioButton
    private lateinit var radioEnglish : RadioButton
    private lateinit var temperatureUnitGroup: RadioGroup
    private lateinit var radioCelsius: RadioButton
    private lateinit var radioKelvin: RadioButton
    private lateinit var radioFahrenheit: RadioButton
    private lateinit var windSpeedUnitGroup: RadioGroup
    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        languageGroup = view.findViewById(R.id.language_group)
        radioArabic = view.findViewById(R.id.language_arabic)
        radioEnglish = view.findViewById(R.id.language_english)

        setupLanguageSelection()

        temperatureUnitGroup = view.findViewById(R.id.temperature_unit_group)
        radioCelsius = view.findViewById(R.id.temperature_celsius)
        radioKelvin = view.findViewById(R.id.temperature_kelvin)
        radioFahrenheit = view.findViewById(R.id.temperature_fahrenheit)

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
        settingsViewModel.selectedTemperatureUnit.observe(viewLifecycleOwner) { unit ->
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
            settingsViewModel.setTemperatureUnit(selectedUnit)
        }
    }

    private fun setupLanguageSelection() {
        // Load the previously selected language
        val selectedLanguage = LanguageUtils.getPersistedLanguage(requireContext())
        when (selectedLanguage) {
            "ar" -> languageGroup.check(R.id.language_arabic)
            "en" -> languageGroup.check(R.id.language_english)
        }

        languageGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.language_arabic -> {
                    settingsViewModel.setLanguage("ar")
                    LanguageUtils.setAppLocale(requireContext(), "ar")
                    requireActivity().recreate()
                }
                R.id.language_english -> {
                    settingsViewModel.setLanguage("en")
                    LanguageUtils.setAppLocale(requireContext(), "en")
                    requireActivity().recreate()
                }
            }
        }
    }
}