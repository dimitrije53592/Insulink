package com.dj.insulink.feature.friends.data.repository

import com.dj.insulink.feature.friends.data.room.dao.FriendDao
import com.dj.insulink.feature.friends.data.room.entity.FriendEntity
import com.dj.insulink.feature.friends.domain.models.Friend
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

class FriendRepositoryTest {

    private val dao: FriendDao = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk()
    private lateinit var repository: FriendRepository

    @Before
    fun setUp() {
        repository = FriendRepository(dao, firestore)
    }

    @Test
    fun `getAllFriendsForUser maps entities to domain`() = runTest {
        val entity = FriendEntity(1, "u1", "f1", "Jane Doe", 100, 5L)
        every { dao.getAllFriendsForUser("u1") } returns flowOf(listOf(entity))

        val result = repository.getAllFriendsForUser("u1").first()

        assertEquals(listOf(Friend(1, "u1", "f1", "Jane Doe", 100, 5L)), result)
    }

    @Test
    fun `addFriend stores the friend locally`() = runTest {
        val friend = Friend(0, "u1", "f1", "Jane Doe", 100, 5L)

        repository.addFriend(friend)

        coVerify { dao.insert(match { it.friendId == "f1" && it.friendName == "Jane Doe" }) }
    }
}
