package com.androidforge.streakhappit.domain.usecase.tracking

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

class MarkHabitCompletionUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long, date: LocalDate, completionCount: Int): Result<Unit> {
        return repository.markHabitCompletion(habitId, date, completionCount)
    }
}