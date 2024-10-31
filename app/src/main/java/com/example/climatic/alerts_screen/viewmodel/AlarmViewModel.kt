package com.example.climatic.alerts_screen.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.climatic.alerts_screen.view.AlarmReceiver

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val _alertDuration = MutableLiveData<Long>()
    val alertDuration: LiveData<Long> get() = _alertDuration

    private val _alarmType = MutableLiveData<String>()
    val alarmType: LiveData<String> get() = _alarmType

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var alarmPendingIntent: PendingIntent? = null

    fun setAlert(duration: Long, type: String) {
        _alertDuration.value = duration
        _alarmType.value = type

        // Create an intent for the AlarmReceiver
        val intent = Intent(getApplication(), AlarmReceiver::class.java).apply {
            putExtra("ALARM_TYPE", type)
        }

        alarmPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm
        val triggerAtMillis = System.currentTimeMillis() + duration
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmPendingIntent!!)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmPendingIntent!!)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun stopAlert() {
        if (alarmPendingIntent != null) {
            alarmManager.cancel(alarmPendingIntent!!)
            alarmPendingIntent = null
            // Optionally reset the alert duration and type
            _alertDuration.value = 0
            _alarmType.value = null
        }
    }
}
