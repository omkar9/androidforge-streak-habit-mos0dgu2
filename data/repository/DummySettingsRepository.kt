package com.androidforge.streakhappit.data.repository

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.AppSettings
import com.androidforge.streakhappit.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DummySettingsRepository @Inject constructor() : SettingsRepository {
    private val _currentSettings = MutableStateFlow(AppSettings())
    override fun getSettingsFlow(): Flow<AppSettings> = _currentSettings.asStateFlow()

    // This method is used by SettingsViewModel for initial load, not exposed in interface as flow is preferred
    suspend fun getSettings(): Result<AppSettings> {
        delay(500) // Simulate network/disk latency
        return Result.Success(_currentSettings.value)
    }

    override suspend fun updateDarkMode(enabled: Boolean): Result<Unit> {
        delay(200) // Simulate network/disk latency
        _currentSettings.update { it.copy(isDarkModeEnabled = enabled) }
        return Result.Success(Unit)
    }

    override suspend fun updateNotifications(enabled: Boolean): Result<Unit> {
        delay(200) // Simulate network/disk latency
        _currentSettings.update { it.copy(areNotificationsEnabled = enabled) }
        return Result.Success(Unit)
    }
}