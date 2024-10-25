package com.example.climatic.home_screen.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        rvHourlyForecast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hourlyAdapter = HourlyAdapter()
        rvHourlyForecast.adapter = hourlyAdapter
        rvDailyForecast = view.findViewById(R.id.rvDailyForecast)
        rvDailyForecast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        dailyAdapter = DailyAdapter()
        rvDailyForecast.adapter = dailyAdapter


        // Initialize ViewModel with Repository
        homeViewModelFactory = HomeViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(requireContext()).dao())
            )
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        lifecycleScope.launch {
            // Collect weather state independently
            homeViewModel.weatherState.collect { state ->
                when (state) {
                    is WeatherState.Loading -> {
                        Log.d("HomeFragment", "Loading weather data...")
                    }

                    is WeatherState.Success -> {
                        // Update the weather data in the UI
                        tvCity.text = state.weather.name
                        tvTemperature.text = "${state.weather.main?.temp}°C"
                        tvWeatherDescription.text = state.weather.weather[0].description


                        val iconId = "w${state.weather.weather[0].icon}"
                        val resourceId = requireContext().resources?.getIdentifier(iconId, "drawable", requireContext().packageName)
                        Glide.with(requireContext())
                            .load(resourceId)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivWeatherIcon)

                        tvHumidityValue.text = "${state.weather.main?.humidity}%"
                        tvWindSpeedValue.text = "${state.weather.wind?.speed} km/h"
                        tvPressureValue.text = "${state.weather.main?.pressure} hPa"
                        tvCloudsValue.text = "${state.weather.clouds?.all}%"
                        tvfeelsLike.text = "${state.weather.main?.feelsLike}°C"


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
                        val time = timeFormatter.format(instant.plusSeconds(state.weather.timezone?.toLong()
                            ?: 0))

                        val sunriseTime = timeFormatter.format(sunriseInstant.plusSeconds(state.weather.timezone?.toLong() ?: 0))
                        val sunsetTime = timeFormatter.format(sunsetInstant.plusSeconds(state.weather.timezone?.toLong() ?: 0))

                        tvSunrise.text = sunriseTime
                        tvSunset.text = sunsetTime
                        tvDate.text = date
                        tvTime.text = time
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
                        val hourlyResponse= state.forecast.toHourlyResponse()
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
        homeViewModel.getWeatherbyCity("Cairo,Egypt")
        homeViewModel.getForecastByCity("Cairo,Egypt")
    }
}
