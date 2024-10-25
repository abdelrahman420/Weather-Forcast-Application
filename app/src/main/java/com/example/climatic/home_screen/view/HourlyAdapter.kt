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
import com.example.climatic.model.dtos.Wlist
import java.text.SimpleDateFormat
import java.util.Locale

class HourlyAdapter : ListAdapter<Wlist, HourlyAdapter.ViewHolder>(HourlyDiffUtil()){

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
        val hourForecast = getItem(position)
        val formattedTime = formatDateTime(hourForecast.dtTxt)
        holder.tvTemperature.text = "${hourForecast.main?.temp} °C"
        holder.tvHour.text = formattedTime

        val iconId = "w${hourForecast.weather.firstOrNull()?.icon}"
        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(iconId, "drawable", context.packageName)
        Glide.with(context)
            .load(resourceId)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.ivIcon)
    }
    private fun formatDateTime(dateTime: String?): String?{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        return outputFormat.format(date)
    }
}



