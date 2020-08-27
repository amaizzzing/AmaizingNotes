package com.amaizzzing.amaizingnotes.utils

import android.content.Context
import android.provider.SyncStateContract
import android.widget.Toast
import androidx.work.*
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.entities.Note
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class MyWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {  override fun doWork(): Result {
    try {
        NotesApplication.instance.getAppDataBase()?.noteDao()?.getNotFinishNotes()
    }catch (e:Exception){
        e.printStackTrace()
        return Result.failure()
    }
    /*val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()    // Set Execution around 05:00:00 AM
    dueDate.set(Calendar.HOUR_OF_DAY, 8)
    dueDate.set(Calendar.MINUTE, 0)
    dueDate.set(Calendar.SECOND, 0)
    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    }
    val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
    val dailyWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
        .addTag("fdfdf")
        .build()
    WorkManager.getInstance()
        .enqueue(dailyWorkRequest)*/
    return Result.success()
}
}