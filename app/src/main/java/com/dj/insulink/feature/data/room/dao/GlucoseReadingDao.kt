package com.dj.insulink.feature.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GlucoseReadingDao {

    @Query("SELECT * FROM glucose_readings")
    fun getAllGlucoseReadings(): Flow<List<GlucoseReadingEntity>>

    @Insert
    suspend fun insert(glucoseReadingEntity: GlucoseReadingEntity)
}