package com.example.climatic.home_screen.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climatic.R
import com.example.climatic.model.dtos.Wlist
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyAdapter : ListAdapter<Wlist, DailyAdapter.ViewHolder>(DailyDiffUtil()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.text_day)
        val temperatureTextView: TextView = itemView.findViewById(R.id.text_temperature)
        val weatherDescriptionTextView: TextView =
            itemView.findViewById(R.id.text_weather_description)
        val humidityTextView: TextView = itemView.findViewById(R.id.tv_HumidityValue)
        val windSpeedTextView: TextView = itemView.findViewById(R.id.tv_WindSpeedValue)
        val pressureTextView: TextView = itemView.findViewById(R.id.tv_PressureValue)
        val cloudsTextView: TextView = itemView.findViewById(R.id.tv_CloudsValue)
        val iconImageView: ImageView = itemView.findViewById(R.id.image_weather_description)
        val tvMax: TextView = itemView.findViewById(R.id.tvMax)
        val tvMin: TextView = itemView.findViewById(R.id.tvMin)
        val detailsLayout: LinearLayout = itemView.findViewById(R.id.details_layout)
        val seeMoreTextView: TextView = itemView.findViewById(R.id.tv_SeeMore)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayForecast = getItem(position)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date = inputFormat.parse(dayForecast.dtTxt)
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        holder.dayTextView.text = outputFormat.format(date)
        holder.temperatureTextView.text = "${dayForecast.main?.temp}"
        holder.weatherDescriptionTextView.text = dayForecast.weather.firstOrNull()?.description
        holder.humidityTextView.text = "${dayForecast.main?.humidity}%"
        holder.windSpeedTextView.text = "${dayForecast.wind?.speed}"
        holder.pressureTextView.text = "${dayForecast.main?.pressure} hPa"
        holder.cloudsTextView.text = "${dayForecast.clouds?.all}%"
        holder.tvMax.text = "${dayForecast.main?.tempMax}°C"
        holder.tvMin.text = "${dayForecast.main?.tempMin}°C"
        val iconId = "w${dayForecast.weather.firstOrNull()?.icon}"
        val context = holder.itemView.context
        val resourceId =
            context.resources.getIdentifier(iconId, "drawable", context.packageName)

        Glide.with(context)
            .load(resourceId)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.iconImageView)

        holder.detailsLayout.visibility = View.GONE // Initially hide details

        holder.seeMoreTextView.setOnClickListener {
            // Toggle the visibility of the details layout
            if (holder.detailsLayout.visibility == View.GONE) {
                holder.detailsLayout.visibility = View.VISIBLE
                holder.seeMoreTextView.text = "See Less" // Change the text to See Less
            } else {
                holder.detailsLayout.visibility = View.GONE
                holder.seeMoreTextView.text = "See More" // Change the text back to See More
            }
        }

    }
}