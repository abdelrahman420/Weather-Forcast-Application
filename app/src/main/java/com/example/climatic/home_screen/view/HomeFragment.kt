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
import com.example.climatic.model.responses.HourlyResponse
import com.example.climatic.model.responses.toHourlyResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

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
        rvHourlyForecast.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hourlyAdapter = HourlyAdapter()
        rvHourlyForecast.adapter = hourlyAdapter

        // Initialize ViewModel with Repository
        homeViewModelFactory = HomeViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(requireContext()).dao())
            )
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        lifecycleScope.launch {
            homeViewModel.forecastState.collect { state ->
                when (state) {
                    is ForecastState.Loading -> {
                        Log.d("HomeFragment", "Loading data...")
                    }

                    is ForecastState.Success -> {
                        Log.d("HomeFragment", "${state.forecast}")
                        tvCity.text = state.forecast.city?.name
                        tvTemperature.text = "${state.forecast.list[0].main?.temp}°C"
                        tvWeatherDescription.text = state.forecast.list[0].weather[0].description

                        tvHumidityValue.text = "${state.forecast.list[0].main?.humidity}%"
                        tvWindSpeedValue.text = "${state.forecast.list[0].wind?.speed} km/h"
                        tvPressureValue.text = "${state.forecast.list[0].main?.pressure} hPa"
                        tvCloudsValue.text = "${state.forecast.list[0].clouds?.all}%"

                        val timestamp = state.forecast.list[0].dt?: 0
                        val instant = Instant.ofEpochSecond(timestamp)

                        // Formatter for date (yyyy-MM-dd)
                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .withZone(ZoneId.of("UTC"))
                        val date = dateFormatter.format(instant)

                        // Formatter for time (HH:mm:ss)
                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                            .withZone(ZoneId.of("UTC"))
                        val time = timeFormatter.format(instant)

                        tvDate.text = date
                        tvTime.text = time



                        Glide.with(requireContext())
                            .load(R.drawable.cloudy)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivWeatherIcon)

                        val hourlyResponse: HourlyResponse = state.forecast.toHourlyResponse()
                        Log.d("HourlyResponse", "Filtered hourly forecasts count: ${hourlyResponse}")
                        hourlyAdapter.submitList(hourlyResponse.list)
                    }

                    is ForecastState.Error -> {
                        Log.e("HomeFragment", "Error: ${state.message}")
                    }
                }
            }
        }
        homeViewModel.getForecastByCity(city = "Cairo")
    }
}
