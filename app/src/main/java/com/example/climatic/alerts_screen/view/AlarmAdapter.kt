package com.example.climatic.alerts_screen.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.climatic.R
import com.example.climatic.alerts_screen.viewmodel.AlarmViewModel
import com.example.climatic.model.dtos.AlarmSettings

class AlarmAdapter(private val viewModel: AlarmViewModel, private val onRemoveAlarm: (Int) -> Unit ) : ListAdapter<AlarmSettings, AlarmAdapter.AlarmViewHolder>(AlarmDiffUtil()) {

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val alarmTimeTextView: TextView = itemView.findViewById(R.id.alarmInfoTextView)
        private val deleteAlarmButton: Button = itemView.findViewById(R.id.deleteAlarmButton)

        fun bind(alarm: AlarmSettings) {
            alarmTimeTextView.text = String.format("%02d:%02d", alarm.hour, alarm.minute)

            deleteAlarmButton.setOnClickListener {
                onRemoveAlarm(alarm.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm)
    }
}
