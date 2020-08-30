package com.amaizzzing.amaizingnotes.utils

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import java.util.*
import java.util.concurrent.TimeUnit

class MyWorkerFactory {
    companion object {
        fun getWorker(worker_type: MY_WORKER_TYPE): WorkRequest {
            when (worker_type) {
                MY_WORKER_TYPE.ONE_TIME -> {
                    val currentDate = Calendar.getInstance()
                    val dueDate = Calendar.getInstance()
                    dueDate.set(Calendar.HOUR_OF_DAY, 7)
                    dueDate.set(Calendar.MINUTE, 22)
                    dueDate.set(Calendar.SECOND, 0)
                    if (dueDate.before(currentDate)) {
                        dueDate.add(Calendar.HOUR_OF_DAY, 0)
                    }
                    val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
                    return OneTimeWorkRequestBuilder<MyWorker>()
                        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                        .addTag("tag").build()
                }
                MY_WORKER_TYPE.PERIODIC -> {
                    val dailyWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.HOURS)
                        .addTag("tag").build()
                    return dailyWorkRequest
                }
            }
        }
    }
}


enum class MY_WORKER_TYPE {
    ONE_TIME,
    PERIODIC
}