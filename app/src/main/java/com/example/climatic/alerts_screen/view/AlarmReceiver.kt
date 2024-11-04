package com.example.climatic.alerts_screen.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.climatic.R
import com.example.climatic.main_screen.view.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm triggered!")

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_alerts_channel",
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "weather_alerts_channel")
            .setContentTitle("Weather Alert")
            .setContentText("It's time for your weather alert!")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)

        val alarmType = intent?.getStringExtra("ALARM_TYPE")
        if (alarmType == "Sound") {
            playSound(context)
        }
    }

    private fun playSound(context: Context?) {
        mediaPlayer?.release()


        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }
}
