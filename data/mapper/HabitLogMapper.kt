package com.androidforge.streakhappit.data.mapper

import com.androidforge.streakhappit.data.local.entity.HabitLogEntity
import com.androidforge.streakhappit.domain.model.CompletionStatus
import com.androidforge.streakhappit.domain.model.HabitLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitLogMapper @Inject constructor() {
    fun mapFromEntity(entity: HabitLogEntity): HabitLog {
        return HabitLog(
            id = entity.id,
            habitId = entity.habitId,
            date = entity.date,
            completionStatus = CompletionStatus.valueOf(entity.completionStatus),
            completionCount = entity.completionCount
        )
    }

    fun mapToEntity(domain: HabitLog): HabitLogEntity {
        return HabitLogEntity(
            id = domain.id,
            habitId = domain.habitId,
            date = domain.date,
            completionStatus = domain.completionStatus.name,
            completionCount = domain.completionCount
        )
    }
}