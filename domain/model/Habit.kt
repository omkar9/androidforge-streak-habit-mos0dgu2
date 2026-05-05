package com.androidforge.streakhappit.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class Habit(
    val id: Long = 0L,
    val name: String,
    val description: String,
    val frequencyType: FrequencyType,
    val specificDays: List<DayOfWeek>, // Only relevant if frequencyType is SpecificDays
    val reminderTime: LocalTime?, // Null if no reminder
    val creationDate: LocalDate,
    val archived: Boolean,
    val color: String, // Hex color string
    val icon: String // Identifier for an icon (e.g., resource name, URL)
)