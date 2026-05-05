package com.androidforge.streakhappit.domain.usecase.habits

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.HabitWithLogsAndStreaks
import com.androidforge.streakhappit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllActiveHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<Result<List<HabitWithLogsAndStreaks>>> {
        return repository.getAllActiveHabitsWithLogs()
    }
}