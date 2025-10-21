package com.dj.insulink.feature.domain.mappers

import com.dj.insulink.feature.data.room.entity.ReminderEntity
import com.dj.insulink.feature.domain.models.Reminder

// Entity -> Domain
fun ReminderEntity.toDomain(): Reminder {
    val reminderTime = this.time
    val currentTime = System.currentTimeMillis()

    val reminderTimeOfDay = reminderTime % (24 * 60 * 60 * 1000)
    val currentTimeOfDay = currentTime % (24 * 60 * 60 * 1000)

    return Reminder(
        id = id,
        userId = userId,
        title = title,
        reminderType = reminderType,
        isDoneForToday = reminderTimeOfDay < currentTimeOfDay,
        time = time
    )
}

// Domain -> Entity
fun Reminder.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        userId = userId,
        title = title,
        reminderType = reminderType,
        isDoneForToday = isDoneForToday,
        time = time
    )
}

fun List<ReminderEntity>.toDomain(): List<Reminder> {
    return map { it.toDomain() }
}

fun List<Reminder>.toEntity(): List<ReminderEntity> {
    return map { it.toEntity() }
}