package com.dj.insulink.feature.domain.mappers

import com.dj.insulink.feature.data.room.entity.ExerciseEntity
import com.dj.insulink.feature.domain.models.Exercise

fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        id = id,
        userId = userId,
        sportName = sportName,
        durationHours = durationHours,
        durationMinutes = durationMinutes,
        glucoseBefore = glucoseBefore,
        glucoseAfter = glucoseAfter
    )
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = id,
        userId = userId,
        sportName = sportName,
        durationHours = durationHours,
        durationMinutes = durationMinutes,
        glucoseBefore = glucoseBefore,
        glucoseAfter = glucoseAfter
    )
}

fun List<ExerciseEntity>.toDomain(): List<Exercise> {
    return map { it.toDomain() }
}

fun List<Exercise>.toEntity(): List<ExerciseEntity> {
    return map { it.toEntity() }
}