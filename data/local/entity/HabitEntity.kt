package com.androidforge.streakhappit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String,
    val frequencyType: String, // Stored as string, converted via TypeConverter
    val specificDays: List<DayOfWeek>, // Stored as string, converted via TypeConverter
    val reminderTime: LocalTime?, // Stored as long (millisOfDay), converted via TypeConverter
    val creationDate: LocalDate,
    val archived: Boolean,
    val color: String,
    val icon: String
)