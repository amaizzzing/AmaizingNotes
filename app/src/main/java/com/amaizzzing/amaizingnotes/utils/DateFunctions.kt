package com.amaizzzing.amaizingnotes.utils

import java.util.*

fun Date.getStartDay(currentDay: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.time = Date(currentDay)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time.time
}

fun Date.getEndDay(currentDay: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.time = Date(currentDay)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)

    return calendar.time.time
}
