package com.dj.insulink.feature.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dj.insulink.feature.data.room.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE userId = :userId")
    fun getAllExercisesForUser(userId: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE userId = :userId")
    suspend fun getAllExercisesForUserOnce(userId: String): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE userId = :userId AND sportName = :sportName")
    fun getExercisesBySportName(userId: String, sportName: String): Flow<List<ExerciseEntity>>

    @Insert
    suspend fun insert(friend: ExerciseEntity): Long

    @Insert
    suspend fun insertAll(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM exercises WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: String)
}