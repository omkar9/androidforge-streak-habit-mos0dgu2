package com.androidforge.streakhappit.data.repository

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.data.local.dao.HabitDao
import com.androidforge.streakhappit.data.local.dao.HabitLogDao
import com.androidforge.streakhappit.data.local.entity.HabitLogEntity
import com.androidforge.streakhappit.data.mapper.HabitLogMapper
import com.androidforge.streakhappit.data.mapper.HabitMapper
import com.androidforge.streakhappit.domain.model.CompletionStatus
import com.androidforge.streakhappit.domain.model.FrequencyType
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.domain.model.HabitLog
import com.androidforge.streakhappit.domain.model.HabitWithLogsAndStreaks
import com.androidforge.streakhappit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao,
    private val habitMapper: HabitMapper,
    private val habitLogMapper: HabitLogMapper
) : HabitRepository {

    override fun getAllActiveHabitsWithLogs(): Flow<Result<List<HabitWithLogsAndStreaks>>> = flow {
        emit(Result.Loading)
        try {
            habitDao.getAllActiveHabits().collect {\ habitEntities ->
                val habitsWithLogsAndStreaks = habitEntities.map { habitEntity ->
                    val habit = habitMapper.mapFromEntity(habitEntity)
                    val logs = habitLogDao.getLogsForHabit(habit.id).first().map(habitLogMapper::mapFromEntity)
                    calculateStreaksAndStats(habit, logs)
                }
                emit(Result.Success(habitsWithLogsAndStreaks))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Unknown error loading habits.", e))
        }
    }

    override fun getHabitById(habitId: Long): Flow<Result<Habit?>> = flow {
        emit(Result.Loading)
        try {
            habitDao.getHabitById(habitId).collect {\ habitEntity ->
                emit(Result.Success(habitEntity?.let(habitMapper::mapFromEntity)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Unknown error loading habit.", e))
        }
    }

    override fun getHabitWithLogsAndStreaks(habitId: Long): Flow<Result<HabitWithLogsAndStreaks?>> = flow {
        emit(Result.Loading)
        try {
            combine(
                habitDao.getHabitById(habitId),
                habitLogDao.getLogsForHabit(habitId)
            ) { habitEntity, logEntities ->
                if (habitEntity == null) {
                    Result.Success(null)
                } else {
                    val habit = habitMapper.mapFromEntity(habitEntity)
                    val logs = logEntities.map(habitLogMapper::mapFromEntity)
                    Result.Success(calculateStreaksAndStats(habit, logs))
                }
            }.collect { emit(it) }
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Unknown error loading habit details.", e))
        }
    }

    override suspend fun addHabit(habit: Habit): Result<Long> = try {
        val id = habitDao.insertHabit(habitMapper.mapToEntity(habit))
        Result.Success(id)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to add habit.", e)
    }

    override suspend fun updateHabit(habit: Habit): Result<Unit> = try {
        habitDao.updateHabit(habitMapper.mapToEntity(habit))
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to update habit.", e)
    }

    override suspend fun deleteHabit(habitId: Long): Result<Unit> = try {
        habitDao.deleteHabit(habitId)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to delete habit.", e)
    }

    override suspend fun archiveHabit(habitId: Long): Result<Unit> = try {
        habitDao.updateHabitArchiveStatus(habitId, true)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to archive habit.", e)
    }

    override suspend fun markHabitCompletion(habitId: Long, date: LocalDate, completionCount: Int): Result<Unit> = try {
        val existingLog = habitLogDao.getLogForHabitAndDate(habitId, date)
        val newLog = existingLog?.copy(completionStatus = CompletionStatus.Completed.name, completionCount = existingLog.completionCount + completionCount)
            ?: HabitLogEntity(habitId = habitId, date = date, completionStatus = CompletionStatus.Completed.name, completionCount = completionCount)
        habitLogDao.insertHabitLog(newLog)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to mark habit as completed.", e)
    }

    override suspend fun markHabitSkipped(habitId: Long, date: LocalDate): Result<Unit> = try {
        val existingLog = habitLogDao.getLogForHabitAndDate(habitId, date)
        val newLog = existingLog?.copy(completionStatus = CompletionStatus.Skipped.name)
            ?: HabitLogEntity(habitId = habitId, date = date, completionStatus = CompletionStatus.Skipped.name, completionCount = 0)
        habitLogDao.insertHabitLog(newLog)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to mark habit as skipped.", e)
    }

    override suspend fun undoHabitCompletion(habitId: Long, date: LocalDate): Result<Unit> = try {
        // Remove the log entry entirely, effectively 'undoing' any status
        habitLogDao.deleteLogForHabitAndDate(habitId, date)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to undo habit completion.", e)
    }

    override suspend fun getHabitLogsForDate(habitId: Long, date: LocalDate): Result<HabitLog?> = try {
        val logEntity = habitLogDao.getLogForHabitAndDate(habitId, date)
        Result.Success(logEntity?.let(habitLogMapper::mapFromEntity))
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Failed to get habit log.", e)
    }

    override fun getAllHabitsForNotifications(): Flow<List<Habit>> = habitDao.getAllActiveHabits().map {
        it.map(habitMapper::mapFromEntity).filter { habit -> habit.reminderTime != null }
    }

    private fun calculateStreaksAndStats(habit: Habit, logs: List<HabitLog>): HabitWithLogsAndStreaks {
        val sortedLogs = logs.sortedBy { it.date }
        val today = LocalDate.now()

        var currentStreak = 0
        var bestStreak = 0
        var totalCompletions = 0
        var tempStreak = 0

        // Filter relevant logs for streak calculation based on frequency
        val relevantLogs = when (habit.frequencyType) {
            FrequencyType.Daily -> sortedLogs
            FrequencyType.Weekly -> sortedLogs.groupBy { it.date.with(DayOfWeek.MONDAY) }.mapNotNull { (_, dailyLogs) ->
                // For weekly, consider it completed if any day in the week was completed
                if (dailyLogs.any { it.completionStatus == CompletionStatus.Completed }) {
                    dailyLogs.first().copy(completionStatus = CompletionStatus.Completed) // Represent week as completed
                } else {
                    null
                }
            }.sortedBy { it.date }
            FrequencyType.SpecificDays -> sortedLogs.filter { habit.specificDays.contains(it.date.dayOfWeek) }
        }

        var lastCompletionDate: LocalDate? = null

        for (log in relevantLogs) {
            if (log.completionStatus == CompletionStatus.Completed) {
                totalCompletions += log.completionCount

                if (lastCompletionDate == null || log.date.minusDays(1) == lastCompletionDate) {
                    tempStreak++
                } else if (log.date > lastCompletionDate.plusDays(1)) {
                    tempStreak = 1
                }
                lastCompletionDate = log.date
                bestStreak = maxOf(bestStreak, tempStreak)
            } else if (log.completionStatus == CompletionStatus.Missed) {
                // Only break streak if it's a missed day that should have been completed
                val shouldHaveCompleted = when (habit.frequencyType) {
                    FrequencyType.Daily -> true
                    FrequencyType.Weekly -> true // Assuming weekly means any day of the week is a potential completion
                    FrequencyType.SpecificDays -> habit.specificDays.contains(log.date.dayOfWeek)
                }
                if (shouldHaveCompleted) {
                    tempStreak = 0
                }
            }
        }

        // Calculate current streak considering today
        currentStreak = 0
        var dateIterator = today
        while (true) {
            val logForDate = relevantLogs.find { it.date == dateIterator }
            val isCompleted = logForDate?.completionStatus == CompletionStatus.Completed
            val shouldBeCompleted = when (habit.frequencyType) {
                FrequencyType.Daily -> true
                FrequencyType.Weekly -> true // For weekly, any day counts towards the week
                FrequencyType.SpecificDays -> habit.specificDays.contains(dateIterator.dayOfWeek)
            }

            if (shouldBeCompleted) {
                if (isCompleted) {
                    currentStreak++
                } else {
                    // If today or a past relevant day is missed, streak breaks
                    break
                }
            } else if (dateIterator < habit.creationDate) {
                // Stop if we go before habit creation date
                break
            }

            if (dateIterator == habit.creationDate) break // Stop if we reach the creation date
            dateIterator = dateIterator.minusDays(1)
        }

        // Ensure current streak is not counted for future days
        if (relevantLogs.any { it.date == today && it.completionStatus == CompletionStatus.Missed }) {
            currentStreak = 0
        }

        val totalPossibleCompletions = (0L..LocalDate.now().toEpochDay() - habit.creationDate.toEpochDay()).count {
            val date = habit.creationDate.plusDays(it)
            when (habit.frequencyType) {
                FrequencyType.Daily -> true
                FrequencyType.Weekly -> true // Simplistic: assume weekly habit can be completed any day of the week
                FrequencyType.SpecificDays -> habit.specificDays.contains(date.dayOfWeek)
            }
        }

        val completionRate = if (totalPossibleCompletions > 0) {
            (totalCompletions.toDouble() / totalPossibleCompletions.toDouble() * 1000).roundToInt() / 10.0
        } else {
            0.0
        }

        return HabitWithLogsAndStreaks(
            habit = habit,
            logs = logs,
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            totalCompletions = totalCompletions,
            completionRate = completionRate
        )
    }
}