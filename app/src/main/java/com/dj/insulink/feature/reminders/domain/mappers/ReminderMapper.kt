package com.dj.insulink.feature.reminders.domain.mappers

import com.dj.insulink.feature.reminders.data.room.entity.ReminderEntity
import com.dj.insulink.feature.reminders.domain.models.Reminder
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

fun ReminderEntity.toDomain(): Reminder {
    val reminderLocalTime = Instant.ofEpochMilli(time)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    return Reminder(
        id = id,
        userId = userId,
        title = title,
        reminderType = reminderType,
        isDoneForToday = reminderLocalTime.isBefore(LocalTime.now()),
        time = time
    )
}

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