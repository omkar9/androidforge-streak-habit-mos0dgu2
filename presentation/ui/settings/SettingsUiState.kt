package com.androidforge.streakhappit.presentation.ui.settings

import com.androidforge.streakhappit.domain.model.AppSettings // NEW: Import AppSettings from domain
import com.androidforge.streakhappit.presentation.ui.common.UiState

// AppSettings data class has been moved to domain/model/AppSettings.kt

sealed class SettingsUiState : UiState<AppSettings>() {
    object Loading : SettingsUiState()
    data class Success(val data: AppSettings) : SettingsUiState()
    data class Error(val message: String, val throwable: Throwable? = null) : SettingsUiState()
    object Empty : SettingsUiState()
    object Offline : SettingsUiState()
}