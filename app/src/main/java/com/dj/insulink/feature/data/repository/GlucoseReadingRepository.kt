package com.dj.insulink.feature.data.repository

import com.dj.insulink.feature.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.data.room.mapper.toDomain
import com.dj.insulink.feature.data.room.mapper.toEntity
import com.dj.insulink.feature.domain.models.GlucoseReading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GlucoseReadingRepository @Inject constructor(
    private val glucoseReadingDao: GlucoseReadingDao
) {

    fun getAllGlucoseReadingsForUser(userId: String): Flow<List<GlucoseReading>> {
       return glucoseReadingDao.getAllGlucoseReadingsForUser(userId).map {
            it.toDomain()
        }
    }

    suspend fun insert(glucoseReading: GlucoseReading) {
        glucoseReadingDao.insert(glucoseReading.toEntity())
    }

    suspend fun delete(glucoseReading: GlucoseReading) {
        glucoseReadingDao.delete(glucoseReading.toEntity())
    }

}