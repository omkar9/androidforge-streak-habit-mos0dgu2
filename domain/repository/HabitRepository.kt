package com.androidforge.streakhappit.domain.repository

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.domain.model.HabitLog
import com.androidforge.streakhappit.domain.model.HabitWithLogsAndStreaks
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface HabitRepository {
    fun getAllActiveHabitsWithLogs(): Flow<Result<List<HabitWithLogsAndStreaks>>>
    fun getHabitById(habitId: Long): Flow<Result<Habit?>>
    fun getHabitWithLogsAndStreaks(habitId: Long): Flow<Result<HabitWithLogsAndStreaks?>>
    suspend fun addHabit(habit: Habit): Result<Long>
    suspend fun updateHabit(habit: Habit): Result<Unit>
    suspend fun deleteHabit(habitId: Long): Result<Unit>
    suspend fun archiveHabit(habitId: Long): Result<Unit>
    suspend fun markHabitCompletion(habitId: Long, date: LocalDate, completionCount: Int): Result<Unit>
    suspend fun markHabitSkipped(habitId: Long, date: LocalDate): Result<Unit>
    suspend fun undoHabitCompletion(habitId: Long, date: LocalDate): Result<Unit>
    suspend fun getHabitLogsForDate(habitId: Long, date: LocalDate): Result<HabitLog?>
    fun getAllHabitsForNotifications(): Flow<List<Habit>>
}