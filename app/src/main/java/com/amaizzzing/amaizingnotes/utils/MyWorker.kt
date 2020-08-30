package com.amaizzzing.amaizingnotes.utils

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.amaizzzing.amaizingnotes.NotesApplication


class MyWorker(val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        try {
            val notification = NotificationCompat.Builder(
                ctx,
                ctx.resources.getString(com.amaizzzing.amaizingnotes.R.string.id_notifications_channel)
            )
                .setSmallIcon(com.amaizzzing.amaizingnotes.R.mipmap.ic_launcher)
                .setContentTitle("AmaizingNotes")
                .setContentText("Не выполненных задач сегодня:5")
                .build()
            NotesApplication.instance.getNotificationManager()?.notify(1, notification)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }
}
