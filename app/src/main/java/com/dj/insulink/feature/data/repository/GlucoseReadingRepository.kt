package com.dj.insulink.feature.data.repository

import android.util.Log
import com.dj.insulink.feature.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.data.room.mapper.toDomain
import com.dj.insulink.feature.data.room.mapper.toEntity
import com.dj.insulink.feature.domain.models.GlucoseReading
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    fun getGlucoseReadingsByDateRange(
        userId: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<GlucoseReading>> {
        return glucoseReadingDao.getGlucoseReadingsByDateRange(userId, startDate, endDate).map {
            it.toDomain()
        }
    }

    suspend fun getDateRange(userId: String): Pair<Long?, Long?> {
        return withContext(Dispatchers.IO) {
            val minDate = glucoseReadingDao.getEarliestTimestamp(userId)
            val maxDate = glucoseReadingDao.getLatestTimestamp(userId)
            Pair(minDate, maxDate)
        }
    }

    suspend fun insert(userId: String, reading: GlucoseReading) {
        withContext(Dispatchers.IO) {
            try {
                val readingWithUniqueId = if (reading.id == 0L) {
                    reading.copy(id = System.currentTimeMillis())
                } else {
                    reading
                }

                glucoseReadingDao.insert(readingWithUniqueId.toEntity())
                pushReadingToFirestoreForUser(userId, readingWithUniqueId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun delete(userId: String, reading: GlucoseReading) {
        withContext(Dispatchers.IO) {
            try {
                glucoseReadingDao.delete(reading.toEntity())
                deleteReadingFromFirestoreForUser(userId, reading)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchAllGlucoseReadingsForUserAndUpdateDatabase(userId: String) {
        withContext(Dispatchers.IO) {
            val fetchedReadings = fetchGlucoseReadingsForUser(userId)
            glucoseReadingDao.deleteAllForUser(userId)
            glucoseReadingDao.insertAll(fetchedReadings.map { it.toEntity() })
        }
    }

    private suspend fun pushReadingToFirestoreForUser(userId: String, reading: GlucoseReading) {
        firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .update(DOCUMENT_FIELD_READINGS, FieldValue.arrayUnion(reading))
            .await()
    }

    private suspend fun deleteReadingFromFirestoreForUser(userId: String, reading: GlucoseReading) {
        try {
            val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)

            val snapshot = userDocumentRef.get().await()
            val readings =
                snapshot.get(DOCUMENT_FIELD_READINGS) as? List<Map<String, Any>> ?: emptyList()

            val updatedReadings = readings.filter { readingMap ->
                val idMatches = (readingMap["id"] as? Number)?.toLong() == reading.id

                !idMatches
            }

            if (readings.size != updatedReadings.size) {
                userDocumentRef.update(DOCUMENT_FIELD_READINGS, updatedReadings).await()
            } else {
                Log.w("GlucoseRepository", "No reading found with ID ${reading.id} to delete")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
