package com.dj.insulink.core.di

import android.content.Context
import com.dj.insulink.feature.data.repository.MealRepositoryFirebase
import com.dj.insulink.feature.data.room.InitialData
import com.dj.insulink.feature.data.room.InsulinkDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = InsulinkDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideGlucoseReadingDao(database: InsulinkDatabase) = database.glucoseReadingDao()

    @Provides
    @Singleton
    fun provideFriendDao(database: InsulinkDatabase) = database.friendDao()

    @Provides
    @Singleton
    fun provideReminderDao(database: InsulinkDatabase) = database.reminderDao()

    @Provides
    @Singleton
    fun provideMealDao(database: InsulinkDatabase) = database.mealDao()

    @Provides
    @Singleton
    fun provideIngredientDao(database: InsulinkDatabase) = database.ingredientDao()

    @Provides
    @Singleton
    fun provideMealIngredientDao(database: InsulinkDatabase) = database.mealIngredientDao()

    @Provides
    @Singleton
    fun provideExerciseDao(database: InsulinkDatabase) = database.exerciseDao()

    @Provides
    @Singleton
    fun provideMealRepository(
        mealDao: com.dj.insulink.feature.data.room.dao.MealDao,
        ingredientDao: com.dj.insulink.feature.data.room.dao.IngredientDao,
        mealIngredientDao: com.dj.insulink.feature.data.room.dao.MealIngredientDao,
        firestore: com.google.firebase.firestore.FirebaseFirestore,
        foodApiRepository: com.dj.insulink.feature.data.repository.FoodApiRepository
    ): MealRepositoryFirebase = MealRepositoryFirebase(mealDao, mealIngredientDao, ingredientDao, firestore, foodApiRepository)

    @Provides
    @Singleton
    fun provideInitialData(
        ingredientDao: com.dj.insulink.feature.data.room.dao.IngredientDao
    ): InitialData = InitialData(ingredientDao)
}