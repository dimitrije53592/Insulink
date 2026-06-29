package com.dj.insulink.feature.reminders.domain.mappers

import com.dj.insulink.feature.reminders.data.room.entity.ReminderEntity
import com.dj.insulink.feature.reminders.domain.models.Reminder
import com.dj.insulink.feature.reminders.domain.models.ReminderType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

class ReminderMapperTest {

    private val entity = ReminderEntity(
        id = 2, userId = "u1", title = "Take insulin",
        reminderType = ReminderType.INSULIN_REMINDER, isDoneForToday = false, time = 5000L
    )

    private fun expectedIsDoneForToday(time: Long): Boolean =
        Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalTime().isBefore(LocalTime.now())

    @Test
    fun `entity maps to domain preserving scalar fields`() {
        val domain = entity.toDomain()
        assertEquals(entity.id, domain.id)
        assertEquals(entity.userId, domain.userId)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.reminderType, domain.reminderType)
        assertEquals(entity.time, domain.time)
    }

    @Test
    fun `entity maps to domain computing isDoneForToday from the time of day`() {
        val domain = entity.toDomain()
        assertEquals(expectedIsDoneForToday(entity.time), domain.isDoneForToday)
    }

    @Test
    fun `domain maps to entity preserving the explicit isDoneForToday flag`() {
        val domain = Reminder(
            id = 2, userId = "u1", title = "Take insulin",
            reminderType = ReminderType.INSULIN_REMINDER, isDoneForToday = true, time = 5000L
        )
        assertEquals(entity.copy(isDoneForToday = true), domain.toEntity())
    }

    @Test
    fun `list mappers map every element`() {
        val entities = listOf(entity, entity.copy(id = 3, title = "Check sugar"))
        val domains = entities.toDomain()
        assertEquals(2, domains.size)
        assertEquals(listOf(2L, 3L), domains.map { it.id })
    }
}
