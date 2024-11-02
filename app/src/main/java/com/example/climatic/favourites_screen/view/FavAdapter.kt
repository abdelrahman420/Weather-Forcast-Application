package com.example.climatic.favourites_screen.view

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.climatic.R
import com.example.climatic.home_screen.view.DailyAdapter.ViewHolder
import com.example.climatic.model.dtos.Favourites
import java.io.IOException
import java.util.Locale

class FavAdapter(
    private val myListener: (Favourites) -> Unit,
    private val removeListener: (Favourites) -> Unit
) : ListAdapter<Favourites, FavAdapter.ViewHolder>(FavDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fav, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavAdapter.ViewHolder, position: Int) {
        val itemFav = getItem(position)
        holder.tvLocationName.text = getAddress(holder.itemView.context, itemFav.lat, itemFav.lon)
        holder.itemView.setOnClickListener {
            myListener(itemFav)
        }
        holder.fabRemoveFavorite.setOnClickListener {
            removeListener(itemFav)
        }
        holder.itemView.setOnClickListener {
            myListener(itemFav)
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLocationName: TextView = itemView.findViewById(R.id.tvLocationName)
        val fabRemoveFavorite: ImageView = itemView.findViewById(R.id.fabRemoveFavorite)
    }

    private fun getAddress(context: Context, lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].subAdminArea ?: ""
            } else {
                "Location not found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Location not available"
        }
    }
}

