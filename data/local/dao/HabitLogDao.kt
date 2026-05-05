package com.androidforge.streakhappit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androidforge.streakhappit.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLogEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHabitLog(log: HabitLogEntity)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY date ASC")
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getLogForHabitAndDate(habitId: Long, date: LocalDate): HabitLogEntity?

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteLogForHabitAndDate(habitId: Long, date: LocalDate)

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getAllLogsForDate(date: LocalDate): Flow<List<HabitLogEntity>>
}