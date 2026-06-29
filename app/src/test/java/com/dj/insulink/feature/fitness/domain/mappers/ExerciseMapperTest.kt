package com.dj.insulink.feature.fitness.domain.mappers

import com.dj.insulink.feature.fitness.data.room.entity.ExerciseEntity
import com.dj.insulink.feature.fitness.domain.model.Exercise
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseMapperTest {

    private val domain = Exercise(
        id = 3, userId = "u1", sportName = "Running",
        durationHours = 1, durationMinutes = 30, glucoseBefore = 150, glucoseAfter = 110
    )
    private val entity = ExerciseEntity(
        id = 3, userId = "u1", sportName = "Running",
        durationHours = 1, durationMinutes = 30, glucoseBefore = 150, glucoseAfter = 110
    )

    @Test
    fun `entity maps to domain`() {
        assertEquals(domain, entity.toDomain())
    }

    @Test
    fun `domain maps to entity`() {
        assertEquals(entity, domain.toEntity())
    }

    @Test
    fun `round trip preserves the exercise`() {
        assertEquals(domain, domain.toEntity().toDomain())
    }

    @Test
    fun `list mappers map every element`() {
        val entities = listOf(entity, entity.copy(id = 4, sportName = "Cycling"))
        val domains = entities.toDomain()
        assertEquals(2, domains.size)
        assertEquals(entities, domains.toEntity())
    }
}
