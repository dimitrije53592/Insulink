package com.dj.insulink.core.utils

import java.util.Calendar

fun combineDateAndTime(dateMillis: Long, existingTimestamp: Long): Long {
    val existingCalendar = Calendar.getInstance().apply { timeInMillis = existingTimestamp }
    val newCalendar = Calendar.getInstance().apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, existingCalendar.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, existingCalendar.get(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return newCalendar.timeInMillis
}

fun combineTimeWithDate(hour: Int, minute: Int, existingTimestamp: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = existingTimestamp
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}