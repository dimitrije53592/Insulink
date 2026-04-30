package com.dj.insulink.feature.reminders.data.repository

import android.util.Log
import com.dj.insulink.feature.reminders.data.room.dao.ReminderDao
import com.dj.insulink.feature.reminders.domain.mappers.toDomain
import com.dj.insulink.feature.reminders.domain.mappers.toEntity
import com.dj.insulink.feature.reminders.domain.models.Reminder
import com.dj.insulink.feature.reminders.domain.models.ReminderType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val firestore: FirebaseFirestore
) {

    fun getAllRemindersForUser(userId: String): Flow<List<Reminder>> {
        return reminderDao.getAllRemindersForUser(userId).map { entities ->
            entities.toDomain().sortedBy { reminder ->
                Instant.ofEpochMilli(reminder.time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime()
            }
        }
    }

    suspend fun insert(userId: String, reminder: Reminder): Long {
        val reminderWithUniqueId = if (reminder.id == DEFAULT_REMINDER_ID) {
            reminder.copy(id = System.currentTimeMillis())
        } else {
            reminder
        }

        withContext(Dispatchers.IO) {
            try {
                reminderDao.insert(reminderWithUniqueId.toEntity())
                pushReminderToFirestoreForUser(userId, reminderWithUniqueId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return reminderWithUniqueId.id
    }

    suspend fun delete(userId: String, reminder: Reminder) {
        withContext(Dispatchers.IO) {
            try {
                reminderDao.delete(reminder.toEntity())
                deleteReminderFromFirestoreForUser(userId, reminder)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchAllRemindersForUserAndUpdateDatabase(userId: String) {
        withContext(Dispatchers.IO) {
            val fetchedReminders = fetchRemindersForUser(userId)
            reminderDao.deleteAllForUser(userId)
            reminderDao.insertAll(fetchedReminders.map { it.toEntity() })
        }
    }

    private suspend fun pushReminderToFirestoreForUser(userId: String, reminder: Reminder) {
        firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .update(DOCUMENT_FIELD_REMINDERS, FieldValue.arrayUnion(reminder))
            .await()
    }

    private suspend fun deleteReminderFromFirestoreForUser(userId: String, reminder: Reminder) {
        try {
            val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)

            val snapshot = userDocumentRef.get().await()
            val reminders =
                snapshot.get(DOCUMENT_FIELD_REMINDERS) as? List<Map<String, Any>> ?: emptyList()

            val updatedReminders = reminders.filter { reminderMap ->
                val idMatches = (reminderMap["id"] as? Number)?.toLong() == reminder.id

                !idMatches
            }

            if (reminders.size != updatedReminders.size) {
                userDocumentRef.update(DOCUMENT_FIELD_REMINDERS, updatedReminders).await()
            } else {
                Log.w("ReminderRepository", "No reminder found with ID ${reminder.id} to delete")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchRemindersForUser(userId: String): List<Reminder> {
        val document = firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .get()
            .await()

        val remindersData =
            document.get(DOCUMENT_FIELD_REMINDERS) as? List<Map<String, Any>> ?: emptyList()

        return remindersData.map { reminderMap ->
            val reminderTime = (reminderMap["time"] as? Number)?.toLong() ?: 0
            val reminderLocalTime = Instant.ofEpochMilli(reminderTime)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()

            Reminder(
                id = (reminderMap["id"] as? Number)?.toLong() ?: 0,
                userId = reminderMap["userId"] as? String ?: "",
                title = reminderMap["title"] as? String ?: "",
                reminderType = ReminderType.fromName(reminderMap["reminderType"] as? String)
                    ?: ReminderType.MEAL_REMINDER,
                isDoneForToday = reminderLocalTime.isBefore(LocalTime.now()),
                time = reminderTime,
            )
        }
    }
}

private const val COLLECTION_NAME_USERS = "users"
private const val DOCUMENT_FIELD_REMINDERS = "reminders"
private const val DEFAULT_REMINDER_ID = 0L