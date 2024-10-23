package com.example.climatic.home_screen.view

import androidx.recyclerview.widget.DiffUtil
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.HourlyForecast

class HourlyDiffUtil: DiffUtil.ItemCallback<HourlyForecast>() {
    override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
        return oldItem == newItem
    }
}