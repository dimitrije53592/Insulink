package com.dj.insulink.feature.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dj.insulink.feature.data.room.dao.FriendDao
import com.dj.insulink.feature.data.room.dao.GlucoseReadingDao
import com.dj.insulink.feature.data.room.dao.ReminderDao
import com.dj.insulink.feature.data.room.entity.FriendEntity
import com.dj.insulink.feature.data.room.dao.IngredientDao
import com.dj.insulink.feature.data.room.dao.MealDao
import com.dj.insulink.feature.data.room.dao.MealIngredientDao
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
        MealIngredientEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class InsulinkDatabase : RoomDatabase() {
    abstract fun glucoseReadingDao(): GlucoseReadingDao
    abstract fun friendDao(): FriendDao
    abstract fun reminderDao(): ReminderDao
    abstract fun mealDao(): MealDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun mealIngredientDao(): MealIngredientDao

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