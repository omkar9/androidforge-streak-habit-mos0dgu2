package com.androidforge.streakhappit.domain.repository

import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettingsFlow(): Flow<AppSettings> // Expose as a flow
    suspend fun updateDarkMode(enabled: Boolean): Result<Unit>
    suspend fun updateNotifications(enabled: Boolean): Result<Unit>
}