package com.dj.insulink.feature.reminders.data.repository

import com.dj.insulink.feature.reminders.data.room.dao.ReminderDao
import com.dj.insulink.feature.reminders.data.room.entity.ReminderEntity
import com.dj.insulink.feature.reminders.domain.models.Reminder
import com.dj.insulink.feature.reminders.domain.models.ReminderType
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

class ReminderRepositoryTest {

    private val dao: ReminderDao = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk()
    private lateinit var repository: ReminderRepository

    @Before
    fun setUp() {
        repository = ReminderRepository(dao, firestore)
    }

    private fun localTimeOf(time: Long): LocalTime =
        Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalTime()

    @Test
    fun `getAllRemindersForUser maps and sorts by time of day`() = runTest {
        val later = ReminderEntity(1, "u1", "Lunch", ReminderType.MEAL_REMINDER, false, 43_200_000L)   // 12:00 UTC
        val earlier = ReminderEntity(2, "u1", "Insulin", ReminderType.INSULIN_REMINDER, false, 36_000_000L) // 10:00 UTC
        every { dao.getAllRemindersForUser("u1") } returns flowOf(listOf(later, earlier))

        val result = repository.getAllRemindersForUser("u1").first()

        assertEquals(2, result.size)
        val times = result.map { localTimeOf(it.time) }
        assertEquals(times.sorted(), times) // ascending by time of day
    }

    @Test
    fun `insert assigns a generated id for a new reminder and returns it`() = runTest {
        val reminder = Reminder(0, "u1", "Lunch", ReminderType.MEAL_REMINDER, false, 1000L)

        val returnedId = repository.insert("u1", reminder)

        assertTrue(returnedId != 0L)
        coVerify { dao.insert(match { it.id == returnedId }) }
    }

    @Test
    fun `insert keeps an existing id`() = runTest {
        val reminder = Reminder(5, "u1", "Lunch", ReminderType.MEAL_REMINDER, false, 1000L)

        val returnedId = repository.insert("u1", reminder)

        assertEquals(5L, returnedId)
        coVerify { dao.insert(match { it.id == 5L }) }
    }

    @Test
    fun `delete removes the reminder locally`() = runTest {
        val reminder = Reminder(5, "u1", "Lunch", ReminderType.MEAL_REMINDER, false, 1000L)

        repository.delete("u1", reminder)

        coVerify { dao.delete(match { it.id == 5L }) }
    }
}
