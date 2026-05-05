package com.androidforge.streakhappit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androidforge.streakhappit.data.local.entity.HabitEntity
import com.androidforge.streakhappit.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Long)

    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: Long): Flow<HabitEntity?>

    @Query("SELECT * FROM habits WHERE archived = 0 ORDER BY creationDate DESC")
    fun getAllActiveHabits(): Flow<List<HabitEntity>>

    @Query("UPDATE habits SET archived = :archived WHERE id = :habitId")
    suspend fun updateHabitArchiveStatus(habitId: Long, archived: Boolean)

    // For retrieving a habit along with its logs for the details screen
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitWithLogs(habitId: Long): Flow<HabitEntity?>
}