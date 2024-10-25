package com.example.climatic.home_screen.view

import androidx.recyclerview.widget.DiffUtil
import com.example.climatic.model.dtos.Wlist

class HourlyDiffUtil: DiffUtil.ItemCallback<Wlist>() {
    override fun areItemsTheSame(oldItem: Wlist, newItem: Wlist): Boolean {
        return oldItem.dtTxt == newItem.dtTxt
    }

    override fun areContentsTheSame(oldItem: Wlist, newItem: Wlist): Boolean {
        return oldItem == newItem
    }
}