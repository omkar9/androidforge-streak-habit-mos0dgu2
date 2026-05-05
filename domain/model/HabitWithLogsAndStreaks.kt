package com.androidforge.streakhappit.domain.model

data class HabitWithLogsAndStreaks(
    val habit: Habit,
    val logs: List<HabitLog>,
    val currentStreak: Int,
    val bestStreak: Int,
    val totalCompletions: Int,
    val completionRate: Double // Percentage completion
)