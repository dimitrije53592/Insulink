package com.dj.insulink.feature.data.room.mapper

import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import com.dj.insulink.feature.domain.models.GlucoseReading

// Entity -> Domain
fun GlucoseReadingEntity.toDomain(): GlucoseReading {
    return GlucoseReading(
        id = id,
        userId = userId,
        timestamp = timestamp,
        value = value,
        comment = comment
    )
}

// Domain -> Entity
fun GlucoseReading.toEntity(): GlucoseReadingEntity {
    return GlucoseReadingEntity(
        id = id,
        userId = userId,
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