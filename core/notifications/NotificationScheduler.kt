package com.androidforge.streakhappit.core.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.androidforge.streakhappit.core.common.Constants
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for habit reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleReminder(id: Int, time: LocalTime, title: String, message: String) {
        // Cancel any existing reminder for this ID first to update it
        cancelReminder(id)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", id)
            putExtra("NOTIFICATION_TITLE", title)
            putExtra("NOTIFICATION_MESSAGE", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If the time has already passed for today, schedule for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, // Repeat daily
            pendingIntent
        )
    }

    fun cancelReminder(id: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    // Helper for re-scheduling all reminders (e.g., after boot or timezone change)
    // This will require fetching all habits with reminders from the repository
    suspend fun rescheduleAllReminders(habits: List<com.androidforge.streakhappit.domain.model.Habit>) {
        habits.forEach { habit ->
            habit.reminderTime?.let {\ time ->
                scheduleReminder(habit.id.toInt(), time, "Time to do your habit!", "Don't forget to complete '${habit.name}' today.")
            }
        }
    }
}

// AlarmReceiver.kt - This would be a separate file in core/notifications/ or core/broadcast/
// For simplicity, I'll include it here as a conceptual part of the notification system.
// In a real app, it would be in its own file.

import android.content.BroadcastReceiver
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val title = intent.getStringExtra("NOTIFICATION_TITLE") ?: "Reminder"
        val message = intent.getStringExtra("NOTIFICATION_MESSAGE") ?: "It's time for your habit!"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification) // TODO: Add a proper notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // TODO: Add an intent to open the app or a specific habit screen when notification is tapped
        // val launchIntent = Intent(context, MainActivity::class.java)
        // val contentPendingIntent = PendingIntent.getActivity(context, notificationId, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        // builder.setContentIntent(contentPendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }
}