package com.example.climatic.alerts_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climatic.R
import com.example.climatic.alerts_screen.viewmodel.AlarmViewModel
import java.util.Calendar

class AlarmFragment : Fragment() {

    private val viewModel: AlarmViewModel by viewModels()
    private lateinit var timePicker: TimePicker
    private lateinit var setAlarmButton: Button
    private lateinit var alarmTypeSpinner: Spinner
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        timePicker = view.findViewById(R.id.timePicker)
        setAlarmButton = view.findViewById(R.id.setAlarmButton)
        alarmTypeSpinner = view.findViewById(R.id.alarmTypeSpinner)
        alarmRecyclerView = view.findViewById(R.id.alarmRecyclerView)

        alarmAdapter = AlarmAdapter(viewModel,
            { alarmId ->
                viewModel.removeAlarm(alarmId)
            }
        )
        alarmRecyclerView.adapter = alarmAdapter
        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.alarms.observe(viewLifecycleOwner) { alarms ->
            alarmAdapter.submitList(alarms)
            alarmAdapter.notifyDataSetChanged()
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.alarm_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            alarmTypeSpinner.adapter = adapter
        }

        setAlarmButton.setOnClickListener {
            setAlarm()
        }
        return view
    }

    private fun setAlarm() {
        val hour = timePicker.hour
        val minute = timePicker.minute
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        val duration = calendar.timeInMillis - System.currentTimeMillis()
        val selectedAlarmType = alarmTypeSpinner.selectedItem.toString()
        if (duration > 0) {
            viewModel.setAlert(duration, selectedAlarmType, hour, minute)
            Toast.makeText(requireContext(), "Alarm set for $hour:$minute with type $selectedAlarmType", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please select a future time.", Toast.LENGTH_SHORT).show()
        }
    }


}
