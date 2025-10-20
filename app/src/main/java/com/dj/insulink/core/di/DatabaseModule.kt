package com.dj.insulink.core.di

import android.content.Context
import com.dj.insulink.feature.data.repository.MealRepository
import com.dj.insulink.feature.data.repository.MealRepositoryImpl
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
    fun provideMealDao(database: InsulinkDatabase) = database.mealDao()
    
    @Provides
    @Singleton
    fun provideIngredientDao(database: InsulinkDatabase) = database.ingredientDao()
    
    @Provides
    @Singleton
    fun provideMealIngredientDao(database: InsulinkDatabase) = database.mealIngredientDao()
    
    @Provides
    @Singleton
    fun provideMealRepository(
        mealDao: com.dj.insulink.feature.data.room.dao.MealDao,
        ingredientDao: com.dj.insulink.feature.data.room.dao.IngredientDao,
        mealIngredientDao: com.dj.insulink.feature.data.room.dao.MealIngredientDao
    ): MealRepository = MealRepositoryImpl(mealDao, ingredientDao, mealIngredientDao)
    
    @Provides
    @Singleton
    fun provideInitialData(
        ingredientDao: com.dj.insulink.feature.data.room.dao.IngredientDao
    ): InitialData = InitialData(ingredientDao)
}