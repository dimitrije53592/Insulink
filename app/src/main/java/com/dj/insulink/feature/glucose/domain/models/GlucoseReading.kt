package com.dj.insulink.feature.glucose.domain.models

data class GlucoseReading(
    val id: Long,
    val userId: String,
    val timestamp: Long,
    val value: Int,
    val comment: String
)