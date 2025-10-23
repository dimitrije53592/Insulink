package com.dj.insulink.feature.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dj.insulink.feature.data.room.dao.ExerciseDao
import com.dj.insulink.feature.data.room.dao.FriendDao
import com.dj.insulink.feature.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.data.room.dao.ReminderDao
import com.dj.insulink.feature.data.room.entity.FriendEntity
import com.dj.insulink.feature.data.room.dao.IngredientDao
import com.dj.insulink.feature.data.room.dao.MealDao
import com.dj.insulink.feature.data.room.dao.MealIngredientDao
import com.dj.insulink.feature.data.room.entity.ExerciseEntity
import com.dj.insulink.feature.data.room.entity.GlucoseReadingEntity
import com.dj.insulink.feature.data.room.entity.ReminderEntity
import com.dj.insulink.feature.data.room.entity.IngredientEntity
import com.dj.insulink.feature.data.room.entity.MealEntity
import com.dj.insulink.feature.data.room.entity.MealIngredientEntity

@Database(
    entities = [
        GlucoseReadingEntity::class,
        FriendEntity::class,
        ReminderEntity::class,
        MealEntity::class,
        IngredientEntity::class,
        MealIngredientEntity::class,
        ExerciseEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class InsulinkDatabase : RoomDatabase() {
    abstract fun glucoseReadingDao(): GlucoseReadingDao
    abstract fun friendDao(): FriendDao
    abstract fun reminderDao(): ReminderDao
    abstract fun mealDao(): MealDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun mealIngredientDao(): MealIngredientDao
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        private var INSTANCE: InsulinkDatabase? = null

        // Migration from version 1 to 3 - Add userId column to ingredients and glucose_readings tables
        private val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE ingredients ADD COLUMN userId TEXT")
                db.execSQL("ALTER TABLE glucose_readings ADD COLUMN userId TEXT DEFAULT ''")
                db.execSQL("UPDATE glucose_readings SET userId = '' WHERE userId IS NULL")
                db.execSQL("CREATE TABLE glucose_readings_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userId TEXT NOT NULL, timestamp INTEGER NOT NULL, value INTEGER NOT NULL, comment TEXT NOT NULL)")
                db.execSQL("INSERT INTO glucose_readings_new (id, userId, timestamp, value, comment) SELECT id, userId, timestamp, value, comment FROM glucose_readings")
                db.execSQL("DROP TABLE glucose_readings")
                db.execSQL("ALTER TABLE glucose_readings_new RENAME TO glucose_readings")
            }
        }

        // Migration from version 3 to 4 - Simplified migration for development
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // For development, we'll let the destructive migration handle this
                // This migration is intentionally empty to trigger fallback
            }
        }

        fun getDatabase(context: Context): InsulinkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InsulinkDatabase::class.java,
                    "insulink_database"
                )
                .addMigrations(MIGRATION_1_3, MIGRATION_3_4)
                .fallbackToDestructiveMigration() // For development - remove in production
                .build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }
}