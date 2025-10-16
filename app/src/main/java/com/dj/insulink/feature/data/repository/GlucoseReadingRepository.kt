package com.dj.insulink.feature.data.repository

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
        val rowId = glucoseReadingDao.insert(reading.toEntity())
        pushReadingToFirestoreForUser(userId, reading.copy(id = rowId))
    }

    suspend fun delete(userId: String, reading: GlucoseReading) {
        glucoseReadingDao.delete(reading.toEntity())
        deleteReadingFromFirestoreForUser(userId, reading)
    }

    suspend fun replaceAll(userId: String, readings: List<GlucoseReading>) {
        glucoseReadingDao.deleteAllForUser(userId)
        glucoseReadingDao.insertAll(readings.map { it.toEntity() })
    }

    suspend fun pushReadingToFirestoreForUser(userId: String, reading: GlucoseReading) {
        firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .update(DOCUMENT_FIELD_READINGS, FieldValue.arrayUnion(reading))
            .await()
    }

    suspend fun deleteReadingFromFirestoreForUser(userId: String, reading: GlucoseReading) {
        val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)

        val snapshot = userDocumentRef.get().await()
        val readings = snapshot.get(DOCUMENT_FIELD_READINGS) as? List<Map<String, Any>> ?: emptyList()

        val updatedReadings = readings.filter { readingMap ->
            val idFromMap = readingMap["id"]
            idFromMap.toString() != reading.id.toString()
        }

        userDocumentRef.update(DOCUMENT_FIELD_READINGS, updatedReadings).await()
    }

    suspend fun fetchGlucoseReadingsForUserOnLogin(userId: String): List<GlucoseReading> {
        val snapshot = firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .collection(DOCUMENT_FIELD_READINGS)
            .get()
            .await()

        return snapshot.toObjects(GlucoseReading::class.java)
    }

    fun startRealtimeSync(userId: String) {
        firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .collection(DOCUMENT_FIELD_READINGS)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                for (change in snapshot.documentChanges) {
                    val reading = change.document.toObject(GlucoseReading::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        when (change.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                glucoseReadingDao.insert(reading.toEntity())
                            }

                            DocumentChange.Type.REMOVED -> {
                                glucoseReadingDao.delete(reading.toEntity())
                            }
                        }
                    }
                }
            }

    }
}

private const val COLLECTION_NAME_USERS = "users"
private const val DOCUMENT_FIELD_READINGS = "readings"
