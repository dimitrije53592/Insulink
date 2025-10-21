package com.dj.insulink.feature.data.repository

import com.dj.insulink.feature.data.room.dao.ExerciseDao
import com.dj.insulink.feature.domain.mappers.toDomain
import com.dj.insulink.feature.domain.mappers.toEntity
import com.dj.insulink.feature.domain.models.Exercise
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val firestore: FirebaseFirestore
) {

    fun getAllExercisesForUser(userId: String): Flow<List<Exercise>> {
        return exerciseDao.getAllExercisesForUser(userId).map {
            it.toDomain()
        }
    }

    suspend fun insert(userId: String, exercise: Exercise) {
        withContext(Dispatchers.IO) {
            try {
                val exerciseWithUniqueId = if (exercise.id == 0L) {
                    exercise.copy(id = System.currentTimeMillis())
                } else {
                    exercise
                }

                exerciseDao.insert(exerciseWithUniqueId.toEntity())
                pushExerciseToFirestoreForUser(userId, exerciseWithUniqueId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchAllExercisesForUserAndUpdateDatabase(userId: String) {
        withContext(Dispatchers.IO) {
            val fetchedExercises = fetchExercisesForUser(userId)
            exerciseDao.deleteAllForUser(userId)
            exerciseDao.insertAll(fetchedExercises.map { it.toEntity() })
        }
    }

    private suspend fun pushExerciseToFirestoreForUser(userId: String, exercise: Exercise) {
        firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .update(DOCUMENT_FIELD_EXERCISES, FieldValue.arrayUnion(exercise))
            .await()
    }

    private suspend fun fetchExercisesForUser(userId: String): List<Exercise> {
        val document = firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .get()
            .await()

        val exercisesData = document.get(DOCUMENT_FIELD_EXERCISES) as? List<Map<String, Any>> ?: emptyList()

        return exercisesData.map { exerciseMap ->
            Exercise(
                id = (exerciseMap["id"] as? Number)?.toLong() ?: 0,
                sportName = exerciseMap["sportName"] as? String ?: "",
                durationHours = (exerciseMap["durationHours"] as? Number)?.toInt() ?: 0,
                durationMinutes = (exerciseMap["durationMinutes"] as? Number)?.toInt() ?: 0,
                glucoseBefore = (exerciseMap["glucoseBefore"] as? Number)?.toInt() ?: 0,
                glucoseAfter = (exerciseMap["glucoseAfter"] as? Number)?.toInt() ?: 0,
                userId = exerciseMap["userId"] as? String ?: ""
            )
        }
    }

    fun getExercisesBySportName(userId: String, sportName: String): Flow<List<Exercise>> {
        return exerciseDao.getExercisesBySportName(userId, sportName).map {
            it.toDomain()
        }
    }
}

private const val COLLECTION_NAME_USERS = "users"
private const val DOCUMENT_FIELD_EXERCISES = "exercises"