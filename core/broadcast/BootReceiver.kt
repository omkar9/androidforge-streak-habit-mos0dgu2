package com.androidforge.streakhappit.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.androidforge.streakhappit.core.notifications.NotificationScheduler
import com.androidforge.streakhappit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            Timber.d("BootReceiver: Device booted or package replaced. Rescheduling notifications.")
            coroutineScope.launch {
                try {
                    val habitsWithReminders = habitRepository.getAllHabitsForNotifications().first()
                    notificationScheduler.rescheduleAllReminders(habitsWithReminders)
                    Timber.d("BootReceiver: Rescheduled ${habitsWithReminders.size} habit reminders.")
                } catch (e: Exception) {
                    Timber.e(e, "BootReceiver: Failed to reschedule notifications.")
                }
            }
        }
    }
}