package com.dj.insulink.feature.domain.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GlucoseReading(
    val id: Long,
    val userId: String,
    val timestamp: Long,
    val value: Int,
    val comment: String
)