package com.dj.insulink.feature.glucose.data.repository

import com.dj.insulink.feature.glucose.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.glucose.data.room.entity.GlucoseReadingEntity
import com.dj.insulink.feature.glucose.domain.models.GlucoseReading
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GlucoseReadingRepositoryTest {

    private val dao: GlucoseReadingDao = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk() // strict: any call throws and is swallowed by try/catch
    private lateinit var repository: GlucoseReadingRepository

    @Before
    fun setUp() {
        repository = GlucoseReadingRepository(dao, firestore)
    }

    @Test
    fun `getAllGlucoseReadingsForUser maps entities to domain`() = runTest {
        val entity = GlucoseReadingEntity(1, "u1", 1000L, 120, "c")
        every { dao.getAllGlucoseReadingsForUser("u1") } returns flowOf(listOf(entity))

        val result = repository.getAllGlucoseReadingsForUser("u1").first()

        assertEquals(listOf(GlucoseReading(1, "u1", 1000L, 120, "c")), result)
    }

    @Test
    fun `getGlucoseReadingsByDateRange maps entities to domain`() = runTest {
        val entity = GlucoseReadingEntity(2, "u1", 1500L, 90, "")
        every { dao.getGlucoseReadingsByDateRange("u1", 0L, 2000L) } returns flowOf(listOf(entity))

        val result = repository.getGlucoseReadingsByDateRange("u1", 0L, 2000L).first()

        assertEquals(listOf(GlucoseReading(2, "u1", 1500L, 90, "")), result)
    }

    @Test
    fun `getDateRange returns the earliest and latest timestamps`() = runTest {
        coEveryEarliestLatest(earliest = 100L, latest = 900L)

        assertEquals(Pair(100L, 900L), repository.getDateRange("u1"))
    }

    private fun coEveryEarliestLatest(earliest: Long?, latest: Long?) {
        io.mockk.coEvery { dao.getEarliestTimestamp("u1") } returns earliest
        io.mockk.coEvery { dao.getLatestTimestamp("u1") } returns latest
    }

    @Test
    fun `insert assigns a generated id for a new reading and stores it locally`() = runTest {
        val reading = GlucoseReading(id = 0, userId = "u1", timestamp = 1000L, value = 120, comment = "")

        repository.insert("u1", reading)

        coVerify { dao.insert(match { it.id != 0L && it.value == 120 }) }
    }

    @Test
    fun `insert keeps an existing id`() = runTest {
        val reading = GlucoseReading(id = 7, userId = "u1", timestamp = 1000L, value = 120, comment = "")

        repository.insert("u1", reading)

        coVerify { dao.insert(match { it.id == 7L }) }
    }

    @Test
    fun `delete removes the reading locally`() = runTest {
        val reading = GlucoseReading(id = 7, userId = "u1", timestamp = 1000L, value = 120, comment = "")

        repository.delete("u1", reading)

        coVerify { dao.delete(match { it.id == 7L }) }
    }
}
