package com.amaizzzing.amaizingnotes.utils

import java.util.*

class DateFunctions {
    companion object{
        fun getStartDay(currentDay:Long):Long{
            val calendar = Calendar.getInstance()
            calendar.time.time = currentDay
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.time.time
        }
        fun getEndDay(currentDay:Long):Long{
            val calendar = Calendar.getInstance()
            calendar.time.time = currentDay
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)

            return calendar.time.time
        }
    }

}