package com.example.climatic.home_screen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climatic.R
import com.example.climatic.model.responses.ForecastResponse
import com.example.climatic.model.responses.HourlyForecast

class HourlyAdapter : ListAdapter<HourlyForecast, HourlyAdapter.ViewHolder>(HourlyDiffUtil()){

    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val tvTemperature: TextView = item.findViewById(R.id.tvTemp)
        val ivIcon: ImageView = item.findViewById(R.id.ivIcon)
        val tvHour: TextView = item.findViewById(R.id.tvHour)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_hour, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentForecast = getItem(position)
        val formattedTime = formatDateTime(currentForecast.dt_txt)
        holder.tvTemperature.text = "${currentForecast.temp} °C"
        holder.tvHour.text = formattedTime

        val iconUrl = "http://openweathermap.org/img/wn/${currentForecast.icon}.png"
        Glide.with(holder.itemView.context)
            .load(iconUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.ivIcon)
    }
    private fun formatDateTime(dateTime: String?): String?{
        // Extract time from dt_txt, assuming format "YYYY-MM-DD HH:MM:SS"
        return dateTime?.split(" ")?.get(1) // This returns just the time part
    }
}



