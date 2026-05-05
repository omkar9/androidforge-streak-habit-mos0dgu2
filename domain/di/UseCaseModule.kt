package com.androidforge.streakhappit.domain.di

import com.androidforge.streakhappit.domain.repository.HabitRepository
import com.androidforge.streakhappit.domain.usecase.habits.AddHabitUseCase
import com.androidforge.streakhappit.domain.usecase.habits.ArchiveHabitUseCase
import com.androidforge.streakhappit.domain.usecase.habits.DeleteHabitUseCase
import com.androidforge.streakhappit.domain.usecase.habits.GetAllActiveHabitsUseCase
import com.androidforge.streakhappit.domain.usecase.habits.GetHabitByIdUseCase
import com.androidforge.streakhappit.domain.usecase.habits.GetHabitWithLogsUseCase
import com.androidforge.streakhappit.domain.usecase.habits.UpdateHabitUseCase
import com.androidforge.streakhappit.domain.usecase.notifications.CancelHabitReminderUseCase
import com.androidforge.streakhappit.domain.usecase.notifications.ScheduleHabitReminderUseCase
import com.androidforge.streakhappit.domain.usecase.tracking.MarkHabitCompletionUseCase
import com.androidforge.streakhappit.domain.usecase.tracking.MarkHabitSkippedUseCase
import com.androidforge.streakhappit.domain.usecase.tracking.UndoHabitCompletionUseCase
import com.androidforge.streakhappit.core.notifications.NotificationScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAddHabitUseCase(repository: HabitRepository): AddHabitUseCase {
        return AddHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateHabitUseCase(repository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteHabitUseCase(repository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideArchiveHabitUseCase(repository: HabitRepository): ArchiveHabitUseCase {
        return ArchiveHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHabitByIdUseCase(repository: HabitRepository): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllActiveHabitsUseCase(repository: HabitRepository): GetAllActiveHabitsUseCase {
        return GetAllActiveHabitsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHabitWithLogsUseCase(repository: HabitRepository): GetHabitWithLogsUseCase {
        return GetHabitWithLogsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMarkHabitCompletionUseCase(repository: HabitRepository): MarkHabitCompletionUseCase {
        return MarkHabitCompletionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMarkHabitSkippedUseCase(repository: HabitRepository): MarkHabitSkippedUseCase {
        return MarkHabitSkippedUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUndoHabitCompletionUseCase(repository: HabitRepository): UndoHabitCompletionUseCase {
        return UndoHabitCompletionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideScheduleHabitReminderUseCase(notificationScheduler: NotificationScheduler): ScheduleHabitReminderUseCase {
        return ScheduleHabitReminderUseCase(notificationScheduler)
    }

    @Provides
    @Singleton
    fun provideCancelHabitReminderUseCase(notificationScheduler: NotificationScheduler): CancelHabitReminderUseCase {
        return CancelHabitReminderUseCase(notificationScheduler)
    }
}