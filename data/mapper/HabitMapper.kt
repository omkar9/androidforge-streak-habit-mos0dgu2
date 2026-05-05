package com.androidforge.streakhappit.data.mapper

import com.androidforge.streakhappit.data.local.entity.HabitEntity
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.domain.model.FrequencyType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitMapper @Inject constructor() {
    fun mapFromEntity(entity: HabitEntity): Habit {
        return Habit(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            frequencyType = FrequencyType.valueOf(entity.frequencyType),
            specificDays = entity.specificDays,
            reminderTime = entity.reminderTime,
            creationDate = entity.creationDate,
            archived = entity.archived,
            color = entity.color,
            icon = entity.icon
        )
    }

    fun mapToEntity(domain: Habit): HabitEntity {
        return HabitEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            frequencyType = domain.frequencyType.name,
            specificDays = domain.specificDays,
            reminderTime = domain.reminderTime,
            creationDate = domain.creationDate,
            archived = domain.archived,
            color = domain.color,
            icon = domain.icon
        )
    }
}