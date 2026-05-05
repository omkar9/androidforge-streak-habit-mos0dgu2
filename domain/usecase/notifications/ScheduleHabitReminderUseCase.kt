package com.androidforge.streakhappit.domain.usecase.notifications

import com.androidforge.streakhappit.core.notifications.NotificationScheduler
import com.androidforge.streakhappit.core.common.Result
import java.time.LocalTime
import javax.inject.Inject

class ScheduleHabitReminderUseCase @Inject constructor(
    private val notificationScheduler: NotificationScheduler
) {
    operator fun invoke(habitId: Long, reminderTime: LocalTime, habitName: String): Result<Unit> {
        return try {
            notificationScheduler.scheduleReminder(
                id = habitId.toInt(),
                time = reminderTime,
                title = "Time to do your habit!",
                message = "Don't forget to complete '$habitName' today."
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to schedule reminder.", e)
        }
    }
}