package com.amaizzzing.amaizingnotes.utils

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R

class MyNotificationChannel{
    var notificationManager: NotificationManager =
        NotesApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
            channel.description = description
        } else {
            val builder = NotificationCompat.Builder(NotesApplication.instance)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Notification text")

            val notification: Notification = builder.build()

            val notificationManager = NotesApplication.instance.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }

    }
}