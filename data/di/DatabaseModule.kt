package com.androidforge.streakhappit.data.di

import android.content.Context
import androidx.room.Room
import com.androidforge.streakhappit.core.common.Constants
import com.androidforge.streakhappit.data.local.converter.Converters
import com.androidforge.streakhappit.data.local.dao.HabitDao
import com.androidforge.streakhappit.data.local.dao.HabitLogDao
import com.androidforge.streakhappit.data.local.database.StreakHabitDatabase
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): StreakHabitDatabase {
        return Room.databaseBuilder(
            context,
            StreakHabitDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .addTypeConverter(Converters::class) // Explicitly add converters if not auto-detected by KSP/Room
            .fallbackToDestructiveMigration() // For development, handle migrations properly in production
            .build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: StreakHabitDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideHabitLogDao(database: StreakHabitDatabase): HabitLogDao {
        return database.habitLogDao()
    }
}