package com.dj.insulink.feature.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dj.insulink.feature.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity

@Database(entities = [GlucoseReadingEntity::class], version = 1, exportSchema = false)
abstract class InsulinkDatabase : RoomDatabase() {
    abstract fun glucoseReadingDao(): GlucoseReadingDao

    companion object {
        private var INSTANCE: InsulinkDatabase? = null

        fun getDatabase(context: Context): InsulinkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InsulinkDatabase::class.java,
                    "insulink_database"
                ).build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }
}