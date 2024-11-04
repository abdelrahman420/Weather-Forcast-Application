package com.example.climatic.alerts_screen.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.climatic.alerts_screen.view.AlarmReceiver
import com.example.climatic.model.dtos.AlarmSettings

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val _alarms = MutableLiveData<MutableList<AlarmSettings>>(mutableListOf())
    val alarms: LiveData<MutableList<AlarmSettings>> get() = _alarms

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun setAlert(duration: Long, type: String, hour : Int, minute : Int ) {
        val newId = (_alarms.value?.size ?: 0)
        val newAlarm = AlarmSettings(id = newId, duration = duration, alarmType = type, hour = hour, minute = minute)

        _alarms.value?.add(newAlarm)
        _alarms.value = _alarms.value

        val intent = Intent(getApplication(), AlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", newId)
            putExtra("ALARM_TYPE", type)
        }

        val alarmPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            newId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = System.currentTimeMillis() + duration
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmPendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmPendingIntent)
        }
    }

    fun removeAlarm(alarmId: Int) {
        _alarms.value = _alarms.value?.filterNot { it.id == alarmId }?.toMutableList()
        cancelAlarm(alarmId)
    }

    private fun cancelAlarm(alarmId: Int) {
        val intent = Intent(getApplication(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(getApplication(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }
}
