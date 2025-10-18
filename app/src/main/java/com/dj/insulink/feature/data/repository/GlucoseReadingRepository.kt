package com.dj.insulink.feature.data.repository

import android.util.Log
import com.dj.insulink.feature.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.data.room.mapper.toDomain
import com.dj.insulink.feature.data.room.mapper.toEntity
import com.dj.insulink.feature.domain.models.GlucoseReading
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.collections.map

class GlucoseReadingRepository @Inject constructor(
    private val glucoseReadingDao: GlucoseReadingDao,
    private val firestore: FirebaseFirestore
) {

    fun getAllGlucoseReadingsForUser(userId: String): Flow<List<GlucoseReading>> {
        return glucoseReadingDao.getAllGlucoseReadingsForUser(userId).map {
            it.toDomain()
        }
    }

    suspend fun insert(userId: String, reading: GlucoseReading) {
        glucoseReadingDao.insert(reading.toEntity())
        pushReadingToFirestoreForUser(userId, reading)
    }

    suspend fun delete(userId: String, reading: GlucoseReading) {
        glucoseReadingDao.delete(reading.toEntity())
        deleteReadingFromFirestoreForUser(userId, reading)
    }

    suspend fun fetchAllGlucoseReadingsForUserAndUpdateDatabase(userId: String) {
        val fetchedReadings = fetchGlucoseReadingsForUser(userId)
        Log.d("Sofija", fetchedReadings.toString())
        glucoseReadingDao.deleteAllForUser(userId)
        glucoseReadingDao.insertAll(fetchedReadings.map { it.toEntity() })
    }

    private suspend fun pushReadingToFirestoreForUser(userId: String, reading: GlucoseReading) {
        firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .update(DOCUMENT_FIELD_READINGS, FieldValue.arrayUnion(reading))
            .await()
    }

    private suspend fun deleteReadingFromFirestoreForUser(userId: String, reading: GlucoseReading) {
        val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)

        val snapshot = userDocumentRef.get().await()
        val readings = snapshot.get(DOCUMENT_FIELD_READINGS) as? List<Map<String, Any>> ?: emptyList()

        val updatedReadings = readings.filter { readingMap ->
            val valueMatches = (readingMap["value"] as? Number)?.toInt() == reading.value
            val timestampMatches = (readingMap["timestamp"] as? Number)?.toLong() == reading.timestamp
            val commentMatches = (readingMap["comment"] as? String) == reading.comment
            val userIdMatches = (readingMap["userId"] as? String) == reading.userId

            !(valueMatches && timestampMatches && commentMatches && userIdMatches)
        }

        userDocumentRef.update(DOCUMENT_FIELD_READINGS, updatedReadings).await()
    }

    private suspend fun fetchGlucoseReadingsForUser(userId: String): List<GlucoseReading> {
        val document = firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .get()
            .await()

        val readingsData = document.get("readings") as? List<Map<String, Any>> ?: emptyList()

        return readingsData.map { readingMap ->
            GlucoseReading(
                id = (readingMap["id"] as? Number)?.toLong() ?: 0,
                value = (readingMap["value"] as? Number)?.toInt() ?: 0,
                timestamp = (readingMap["timestamp"] as? Number)?.toLong() ?: 0,
                comment = readingMap["comment"] as? String ?: "",
                userId = readingMap["userId"] as? String ?: "",
            )

        }
    }
}

private const val COLLECTION_NAME_USERS = "users"
private const val DOCUMENT_FIELD_READINGS = "readings"
