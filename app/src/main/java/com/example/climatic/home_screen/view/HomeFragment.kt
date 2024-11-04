package com.example.climatic.home_screen.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climatic.R
import com.example.climatic.home_screen.viewmodel.HomeViewModel
import com.example.climatic.home_screen.viewmodel.HomeViewModelFactory
import com.example.climatic.model.ForecastState
import com.example.climatic.model.WeatherState
import com.example.climatic.model.database.LocalDataSourceImpl
import com.example.climatic.model.database.WeatherDB
import com.example.climatic.model.network.RemoteDataSourceImpl
import com.example.climatic.model.repository.RepositoryImpl
import com.example.climatic.model.responses.getFiveDaysForecast
import com.example.climatic.model.responses.toHourlyResponse
import com.example.climatic.settings_screen.viewmodel.SettingsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvWeatherDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvHumidityValue: TextView
    private lateinit var tvWindSpeedValue: TextView
    private lateinit var tvPressureValue: TextView
    private lateinit var tvCloudsValue: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var rvHourlyForecast: RecyclerView
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var rvDailyForecast: RecyclerView
    private lateinit var tvSunrise: TextView
    private lateinit var tvSunset: TextView
    private lateinit var tvfeelsLike: TextView
    private lateinit var tvTemperatureUnit: TextView
    private lateinit var tvFeelsLikeTempUnit: TextView
    private lateinit var tvWindSpeedUnit: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lattitude: Double = 0.0
    private var longitude: Double = 0.0
    private val REQUEST_LOCATION_CODE = 101
    private var isFavourite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTemperatureUnit = view.findViewById(R.id.tvTemperatureUnit)
        tvCity = view.findViewById(R.id.tvCity)
        tvTemperature = view.findViewById(R.id.tvTemperature)
        tvWeatherDescription = view.findViewById(R.id.tvWeatherDescription)
        tvDate = view.findViewById(R.id.tvDate)
        tvTime = view.findViewById(R.id.tvTime)
        tvHumidityValue = view.findViewById(R.id.tvHumidityValue)
        tvWindSpeedValue = view.findViewById(R.id.tvWindSpeedValue)
        tvPressureValue = view.findViewById(R.id.tvPressureValue)
        tvCloudsValue = view.findViewById(R.id.tvCloudsValue)
        ivWeatherIcon = view.findViewById(R.id.ivWeatherIcon)
        rvHourlyForecast = view.findViewById(R.id.rvHourlyForecast)
        tvSunrise = view.findViewById(R.id.tvsunrise)
        tvSunset = view.findViewById(R.id.tvsunset)
        tvfeelsLike = view.findViewById(R.id.tvFeelsLikeTemp)
        tvFeelsLikeTempUnit = view.findViewById(R.id.tvFeelsLikeTempUnit)
        tvWindSpeedUnit = view.findViewById(R.id.tvWindSpeedValueUnit)
        rvHourlyForecast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hourlyAdapter = HourlyAdapter()
        rvHourlyForecast.adapter = hourlyAdapter
        rvDailyForecast = view.findViewById(R.id.rvDailyForecast)
        rvDailyForecast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        dailyAdapter = DailyAdapter()
        rvDailyForecast.adapter = dailyAdapter

        settingsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingsViewModel::class.java)

        homeViewModelFactory = HomeViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(requireContext()).dao())
            ), settingsViewModel
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        arguments?.let {
            val lat = it.getDouble("lat")
            val lon = it.getDouble("lon")
            isFavourite = it.getBoolean("isFavourite")
            homeViewModel.getWeatherbyLatLon(lat, lon)
            homeViewModel.getForecastByLatLon(lat, lon)
        }

        lifecycleScope.launch {
            homeViewModel.weatherState.collect { state ->
                when (state) {
                    is WeatherState.Loading -> {
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    is WeatherState.Success -> {
                        tvCity.text = state.weather.name
                        tvTemperature.text = "${state.weather.main?.temp}"
                        tvWeatherDescription.text = state.weather.weather[0].description
                        if(settingsViewModel.selectedTemperatureUnit.value == "metric")
                        {
                            tvTemperatureUnit.text = "°C"
                            tvFeelsLikeTempUnit.text = "°C"
                            tvWindSpeedUnit.text = "m/s"

                        }
                        else if(settingsViewModel.selectedTemperatureUnit.value == "standard")
                        {
                            tvTemperatureUnit.text = "°K"
                            tvFeelsLikeTempUnit.text = "°K"
                            tvWindSpeedUnit.text = "m/s"

                        }
                        else
                        {
                            tvTemperatureUnit.text = "°F"
                            tvFeelsLikeTempUnit.text = "°F"
                            tvWindSpeedUnit.text = "m/h"

                        }


                        val iconId = "w${state.weather.weather[0].icon}"
                        val resourceId = requireContext().resources?.getIdentifier(
                            iconId,
                            "drawable",
                            requireContext().packageName
                        )
                        Glide.with(requireContext())
                            .load(resourceId)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivWeatherIcon)

                        Thread {
                            Glide.get(requireContext()).clearDiskCache()
                        }.start()

                        Glide.get(requireContext()).clearMemory()

                        tvHumidityValue.text = "${state.weather.main?.humidity}%"
                        tvWindSpeedValue.text = "${state.weather.wind?.speed}"
                        tvPressureValue.text = "${state.weather.main?.pressure} hPa"
                        tvCloudsValue.text = "${state.weather.clouds?.all}%"
                        tvfeelsLike.text = "${state.weather.main?.feelsLike}"


                        val sunriseTimestamp = state.weather.sys?.sunrise ?: 0
                        val sunsetTimestamp = state.weather.sys?.sunset ?: 0
                        val sunriseInstant = Instant.ofEpochSecond(sunriseTimestamp.toLong())
                        val sunsetInstant = Instant.ofEpochSecond(sunsetTimestamp.toLong())

                        val timestamp = state.weather.dt ?: 0
                        val instant = Instant.ofEpochSecond(timestamp)

                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .withZone(ZoneId.of("UTC"))
                        val date = dateFormatter.format(instant)

                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                            .withZone(ZoneId.of("UTC"))
                        val time = timeFormatter.format(
                            instant.plusSeconds(
                                state.weather.timezone?.toLong()
                                    ?: 0
                            )
                        )

                        val sunriseTime = timeFormatter.format(
                            sunriseInstant.plusSeconds(
                                state.weather.timezone?.toLong() ?: 0
                            )
                        )
                        val sunsetTime = timeFormatter.format(
                            sunsetInstant.plusSeconds(
                                state.weather.timezone?.toLong() ?: 0
                            )
                        )

                        tvSunrise.text = sunriseTime
                        tvSunset.text = sunsetTime
                        tvDate.text = date
                        tvTime.text = time

                        Log.d(TAG, "Weather data updated: ${state.weather}")
                    }

                    is WeatherState.Error -> {
                        Log.e(TAG, "Error: ${state.message}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            homeViewModel.forecastState.collect { state ->
                when (state) {
                    is ForecastState.Loading -> {
                        Log.d("HomeFragment", "Loading forecast data...")
                    }

                    is ForecastState.Success -> {
                        val hourlyResponse = state.forecast.toHourlyResponse()
                        Log.d(TAG, "Filtered hourly forecasts count: ${hourlyResponse}")
                        hourlyAdapter.submitList(hourlyResponse)
                        val daysResponse = state.forecast.getFiveDaysForecast()
                        Log.d(TAG, "Filtered days forecasts count: ${daysResponse}")

                        dailyAdapter.submitList(daysResponse)

                    }

                    is ForecastState.Error -> {
                        Log.e(TAG, "Error: ${state.message}")
                    }
                }
            }
        }
    }

    fun checkPermissions(): Boolean {
        return ((checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
                || (checkSelfPermission(
            requireContext(),
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED))
    }

    fun enableLocationServices() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    longitude = location.lastLocation?.longitude ?: 0.0
                    lattitude = location.lastLocation?.latitude ?: 0.0
                    homeViewModel.getWeatherbyLatLon(
                        location.lastLocation?.latitude!!,
                        location.lastLocation?.longitude!!
                    )
                    homeViewModel.getForecastByLatLon(
                        location.lastLocation?.latitude!!,
                        location.lastLocation?.longitude!!
                    )
                }
            },
            Looper.myLooper()
        )
    }

    fun onRequestPermessionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.size > 1 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (isFavourite == false) {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    getFreshLocation()
                } else {
                    enableLocationServices()
                }
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_LOCATION_CODE
                )
            }
        }
    }
}