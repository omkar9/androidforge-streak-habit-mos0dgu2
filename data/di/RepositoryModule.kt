package com.androidforge.streakhappit.data.di

import com.androidforge.streakhappit.data.repository.DummySettingsRepository
import com.androidforge.streakhappit.data.repository.HabitRepositoryImpl
import com.androidforge.streakhappit.domain.repository.HabitRepository
import com.androidforge.streakhappit.presentation.ui.settings.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(habitRepositoryImpl: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(dummySettingsRepository: DummySettingsRepository): SettingsRepository
}