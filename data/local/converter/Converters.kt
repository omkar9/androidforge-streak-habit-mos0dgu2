package com.androidforge.streakhappit.data.local.converter

import androidx.room.TypeConverter
import com.androidforge.streakhappit.domain.model.CompletionStatus
import com.androidforge.streakhappit.domain.model.FrequencyType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): Long? {
        return time?.toSecondOfDay()?.toLong()
    }

    @TypeConverter\    fun toLocalTime(secondOfDay: Long?): LocalTime? {
        return secondOfDay?.let { LocalTime.ofSecondOfDay(it) }
    }

    @TypeConverter
    fun fromFrequencyType(type: FrequencyType): String {
        return type.name
    }

    @TypeConverter
    fun toFrequencyType(name: String): FrequencyType {
        return FrequencyType.valueOf(name)
    }

    @TypeConverter
    fun fromSpecificDaysList(days: List<DayOfWeek>): String {
        return days.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toSpecificDaysList(data: String): List<DayOfWeek> {
        return if (data.isBlank()) emptyList() else data.split(",").map { DayOfWeek.valueOf(it) }
    }

    @TypeConverter
    fun fromCompletionStatus(status: CompletionStatus): String {
        return status.name
    }

    @TypeConverter
    fun toCompletionStatus(name: String): CompletionStatus {
        return CompletionStatus.valueOf(name)
    }
}