package com.dj.insulink.feature.glucose.domain.mappers

import com.dj.insulink.feature.dataREMOVE.room.mapper.toDomain
import com.dj.insulink.feature.dataREMOVE.room.mapper.toEntity
import com.dj.insulink.feature.glucose.data.room.entity.GlucoseReadingEntity
import com.dj.insulink.feature.glucose.domain.models.GlucoseReading
import org.junit.Assert.assertEquals
import org.junit.Test

class GlucoseMapperTest {

    private val domain = GlucoseReading(id = 7, userId = "u1", timestamp = 1000L, value = 120, comment = "after lunch")
    private val entity = GlucoseReadingEntity(id = 7, userId = "u1", timestamp = 1000L, value = 120, comment = "after lunch")

    @Test
    fun `entity maps to domain field for field`() {
        assertEquals(domain, entity.toDomain())
    }

    @Test
    fun `domain maps to entity field for field`() {
        assertEquals(entity, domain.toEntity())
    }

    @Test
    fun `round trip preserves the reading`() {
        assertEquals(domain, domain.toEntity().toDomain())
    }

    @Test
    fun `list mappers map every element`() {
        val entities = listOf(entity, entity.copy(id = 8, value = 90))
        val domains = entities.toDomain()
        assertEquals(2, domains.size)
        assertEquals(entities, domains.toEntity())
    }
}
