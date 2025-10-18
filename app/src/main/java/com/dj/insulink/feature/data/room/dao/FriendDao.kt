package com.dj.insulink.feature.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dj.insulink.feature.data.room.entity.FriendEntity
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Query("SELECT * FROM friends WHERE userId = :userId")
    fun getAllFriendsForUser(userId: String): Flow<List<FriendEntity>>

    @Query("SELECT * FROM friends WHERE userId = :userId")
    suspend fun getAllFriendsForUserOnce(userId: String): List<FriendEntity>

    @Insert
    suspend fun insert(friend: FriendEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<FriendEntity>)

    @Query("""
        UPDATE friends 
        SET friendLastGlucoseReadingValue = :readingValue, 
            friendsLastGlucoseReadingTime = :timestamp
        WHERE friendId = :friendId AND userId = :userId
    """)
    suspend fun updateLatestReading(
        userId: String,
        friendId: String,
        readingValue: Int,
        timestamp: Long
    )

}