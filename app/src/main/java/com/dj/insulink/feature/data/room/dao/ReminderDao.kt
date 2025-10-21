package com.dj.insulink.feature.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import com.dj.insulink.feature.data.room.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE userId = :userId ORDER BY time")
    fun getAllRemindersForUser(userId: String): Flow<List<ReminderEntity>>

    @Insert
    suspend fun insert(reminderEntity: ReminderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<ReminderEntity>)

    @Delete
    suspend fun delete(reminderEntity: ReminderEntity)

    @Query("DELETE FROM reminders WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: String)
}