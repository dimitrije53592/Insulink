package com.dj.insulink.feature.domain.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GlucoseReading(
    val id: Long,
    val timestamp: Long,
    val value: Int,
    val comment: String
) {
    fun getFormattedDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun getFormattedTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun getFormattedDateTime(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }
}