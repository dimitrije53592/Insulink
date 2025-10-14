package com.dj.insulink.feature.data.room.mapper

import androidx.compose.runtime.clearCompositionErrors
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import com.dj.insulink.feature.domain.models.GlucoseReading

// Entity -> Domain
fun GlucoseReadingEntity.toDomain(): GlucoseReading {
    return GlucoseReading(
        id = id,
        timestamp = timestamp,
        value = value,
        comment = comment
    )
}

// Domain -> Entity
fun GlucoseReading.toEntity(): GlucoseReadingEntity {
    return GlucoseReadingEntity(
        id = id,
        timestamp = timestamp,
        value = value,
        comment = comment
    )
}

fun List<GlucoseReadingEntity>.toDomain(): List<GlucoseReading> {
    return map { it.toDomain() }
}

fun List<GlucoseReading>.toEntity(): List<GlucoseReadingEntity> {
    return map { it.toEntity() }
}