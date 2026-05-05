package com.androidforge.streakhappit.domain.usecase.notifications

import com.androidforge.streakhappit.core.notifications.NotificationScheduler
import com.androidforge.streakhappit.core.common.Result
import javax.inject.Inject

class CancelHabitReminderUseCase @Inject constructor(
    private val notificationScheduler: NotificationScheduler
) {
    operator fun invoke(habitId: Long): Result<Unit> {
        return try {
            notificationScheduler.cancelReminder(habitId.toInt()) // Use habitId as request code
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to cancel reminder.", e)
        }
    }
}