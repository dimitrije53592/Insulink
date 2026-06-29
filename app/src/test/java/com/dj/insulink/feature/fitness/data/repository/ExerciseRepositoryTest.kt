package com.dj.insulink.feature.fitness.data.repository

import com.dj.insulink.feature.fitness.data.room.dao.ExerciseDao
import com.dj.insulink.feature.fitness.data.room.entity.ExerciseEntity
import com.dj.insulink.feature.fitness.domain.model.Exercise
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

class ExerciseRepositoryTest {

    private val dao: ExerciseDao = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk()
    private lateinit var repository: ExerciseRepository

    @Before
    fun setUp() {
        repository = ExerciseRepository(dao, firestore)
    }

    @Test
    fun `getAllExercisesForUser maps entities to domain`() = runTest {
        val entity = ExerciseEntity(1, "u1", "Running", 1, 0, 150, 110)
        every { dao.getAllExercisesForUser("u1") } returns flowOf(listOf(entity))

        val result = repository.getAllExercisesForUser("u1").first()

        assertEquals(listOf(Exercise(1, "u1", "Running", 1, 0, 150, 110)), result)
    }

    @Test
    fun `getExercisesBySportName maps entities to domain`() = runTest {
        val entity = ExerciseEntity(2, "u1", "Cycling", 0, 45, 130, 120)
        every { dao.getExercisesBySportName("u1", "Cycling") } returns flowOf(listOf(entity))

        val result = repository.getExercisesBySportName("u1", "Cycling").first()

        assertEquals(listOf(Exercise(2, "u1", "Cycling", 0, 45, 130, 120)), result)
    }

    @Test
    fun `insert assigns a generated id for a new exercise`() = runTest {
        val exercise = Exercise(0, "u1", "Running", 1, 0, 150, 110)

        repository.insert("u1", exercise)

        coVerify { dao.insert(match { it.id != 0L && it.sportName == "Running" }) }
    }

    @Test
    fun `insert keeps an existing id`() = runTest {
        val exercise = Exercise(9, "u1", "Running", 1, 0, 150, 110)

        repository.insert("u1", exercise)

        coVerify { dao.insert(match { it.id == 9L }) }
    }
}
