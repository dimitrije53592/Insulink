package com.dj.insulink.feature.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GlucoseReadingDao {

    @Query("SELECT * FROM glucose_readings WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllGlucoseReadingsForUser(userId: String): Flow<List<GlucoseReadingEntity>>

    @Insert
    suspend fun insert(glucoseReadingEntity: GlucoseReadingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<GlucoseReadingEntity>)

    @Delete
    suspend fun delete(glucoseReadingEntity: GlucoseReadingEntity)

    @Query("DELETE FROM glucose_readings WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: String)
}