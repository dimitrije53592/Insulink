package com.dj.insulink.core.di

import android.content.Context
import com.dj.insulink.feature.data.room.InsulinkDatabase
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
}