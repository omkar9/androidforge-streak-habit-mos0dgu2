package com.androidforge.streakhappit.domain.usecase.habits

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitByIdUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(habitId: Long): Flow<Result<Habit?>> {
        return repository.getHabitById(habitId)
    }
}