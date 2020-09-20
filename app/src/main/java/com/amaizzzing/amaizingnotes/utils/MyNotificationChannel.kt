package com.amaizzzing.amaizingnotes.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R

class MyNotificationChannel(var notificationManager: NotificationManager) {
    fun createNotificationChannel(
        id: String, name: String,
        description: String
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300)
            notificationManager.createNotificationChannel(channel)
            channel.description = description
        } else {
            val builder = NotificationCompat.Builder(NotesApplication.instance)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Notification text")

            notificationManager.notify(1, builder.build())
        }

    }
}