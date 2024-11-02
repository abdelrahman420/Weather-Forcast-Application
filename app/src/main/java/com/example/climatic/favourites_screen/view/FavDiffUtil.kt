package com.example.climatic.favourites_screen.view

import androidx.recyclerview.widget.DiffUtil
import com.example.climatic.model.dtos.Favourites

class FavDiffUtil : DiffUtil.ItemCallback<Favourites>() {
    override fun areItemsTheSame(oldItem: Favourites, newItem: Favourites): Boolean {
        return oldItem.lat == newItem.lat
    }
    override fun areContentsTheSame(oldItem: Favourites, newItem: Favourites): Boolean {
        return oldItem == newItem
    }
}