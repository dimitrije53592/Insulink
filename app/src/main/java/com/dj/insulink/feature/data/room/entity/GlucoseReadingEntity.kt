package com.dj.insulink.feature.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "glucose_readings")
data class GlucoseReadingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: String,
    val timestamp: Long,
    val value: Int,
    val comment: String
)
