package com.androidforge.streakhappit.domain.usecase.habits

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.domain.repository.HabitRepository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit): Result<Long> {
        return repository.addHabit(habit)
    }
}