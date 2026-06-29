package com.dj.insulink.feature.friends.domain.mappers

import com.dj.insulink.feature.friends.data.room.entity.FriendEntity
import com.dj.insulink.feature.friends.domain.models.Friend
import org.junit.Assert.assertEquals
import org.junit.Test

class FriendMapperTest {

    private val domain = Friend(
        id = 1, userId = "u1", friendId = "f1", friendName = "Jane Doe",
        friendLastGlucoseReadingValue = 105, friendsLastGlucoseReadingTime = 2000L
    )
    private val entity = FriendEntity(
        id = 1, userId = "u1", friendId = "f1", friendName = "Jane Doe",
        friendLastGlucoseReadingValue = 105, friendsLastGlucoseReadingTime = 2000L
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
    fun `nullable reading fields are preserved`() {
        val noReading = domain.copy(friendLastGlucoseReadingValue = null, friendsLastGlucoseReadingTime = null)
        assertEquals(noReading, noReading.toEntity().toDomain())
    }

    @Test
    fun `list mappers map every element`() {
        val entities = listOf(entity, entity.copy(id = 2, friendName = "John Roe"))
        val domains = entities.toDomain()
        assertEquals(2, domains.size)
        assertEquals(entities, domains.toEntity())
    }
}
