package com.example.climatic.alerts_screen.view

import androidx.recyclerview.widget.DiffUtil
import com.example.climatic.model.dtos.AlarmSettings

class AlarmDiffUtil : DiffUtil.ItemCallback<AlarmSettings>() {
    override fun areItemsTheSame(oldItem: AlarmSettings, newItem: AlarmSettings): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlarmSettings, newItem: AlarmSettings): Boolean {
        return oldItem == newItem
    }
}