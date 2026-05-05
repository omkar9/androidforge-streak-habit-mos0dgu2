package com.androidforge.streakhappit.domain.usecase.habits

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.HabitWithLogsAndStreaks
import com.androidforge.streakhappit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitWithLogsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(habitId: Long): Flow<Result<HabitWithLogsAndStreaks?>> {
        return repository.getHabitWithLogsAndStreaks(habitId)
    }
}