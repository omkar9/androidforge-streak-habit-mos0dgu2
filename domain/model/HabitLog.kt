package com.androidforge.streakhappit.domain.model

import java.time.LocalDate

data class HabitLog(
    val id: Long = 0L,
    val habitId: Long,
    val date: LocalDate,
    val completionStatus: CompletionStatus,
    val completionCount: Int // For habits that can be completed multiple times a day
)