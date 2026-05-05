package com.androidforge.streakhappit.domain.usecase.habits

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.repository.HabitRepository
import javax.inject.Inject

class ArchiveHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long): Result<Unit> {
        return repository.archiveHabit(habitId)
    }
}