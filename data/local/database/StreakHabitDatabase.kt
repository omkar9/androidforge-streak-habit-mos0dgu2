package com.androidforge.streakhappit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidforge.streakhappit.data.local.converter.Converters
import com.androidforge.streakhappit.data.local.dao.HabitDao
import com.androidforge.streakhappit.data.local.dao.HabitLogDao
import com.androidforge.streakhappit.data.local.entity.HabitEntity
import com.androidforge.streakhappit.data.local.entity.HabitLogEntity

@Database(
    entities = [HabitEntity::class, HabitLogEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StreakHabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
}