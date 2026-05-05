package com.androidforge.streakhappit.presentation.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidforge.streakhappit.core.common.Result
import com.androidforge.streakhappit.domain.model.AppSettings
import com.androidforge.streakhappit.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Note: The SettingsRepository interface and AppSettings data class have been moved to the domain module.
// This file now references them from the domain module.

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val applicationContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            val isOffline = false // TODO: Replace with actual network connectivity check
            if (isOffline) {
                _uiState.value = SettingsUiState.Offline
                return@launch
            }

            // Call the internal getSettings from the DummySettingsRepository for initial state
            // In a real app, SettingsRepository would ideally expose a direct flow for settings.
            when (val result = (settingsRepository as com.androidforge.streakhappit.data.repository.DummySettingsRepository).getSettings()) {
                is Result.Success -> {
                    _uiState.value = SettingsUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = SettingsUiState.Error(result.message ?: "Failed to load settings.", result.throwable)
                }
                Result.Loading -> { /* Handled by initial state */ }
            }
        }
    }

    fun onDarkModeToggled(enabled: Boolean) {
        viewModelScope.launch {
            if (_uiState.value is SettingsUiState.Success) {
                val currentSettings = (_uiState.value as SettingsUiState.Success).data
                _uiState.update { SettingsUiState.Success(currentSettings.copy(isDarkModeEnabled = enabled)) }
                when (val result = settingsRepository.updateDarkMode(enabled)) {
                    is Result.Error -> {
                        _uiState.update { SettingsUiState.Error(result.message ?: "Failed to update dark mode.", result.throwable) }
                        loadSettings() // Revert UI if update failed
                    }
                    else -> { /* Success is already reflected in UI */ }
                }
            }
        }
    }

    fun onNotificationsToggled(enabled: Boolean) {
        viewModelScope.launch {
            if (_uiState.value is SettingsUiState.Success) {
                val currentSettings = (_uiState.value as SettingsUiState.Success).data
                _uiState.update { SettingsUiState.Success(currentSettings.copy(areNotificationsEnabled = enabled)) }
                when (val result = settingsRepository.updateNotifications(enabled)) {
                    is Result.Error -> {
                        _uiState.update { SettingsUiState.Error(result.message ?: "Failed to update notifications.", result.throwable) }
                        loadSettings() // Revert UI if update failed
                    }
                    else -> { /* Success is already reflected in UI */ }
                }
            }
        }
    }

    fun openPrivacyPolicy() {
        val url = "https://www.example.com/privacy" // TODO: Replace with actual privacy policy URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out Streak Habit!")
            putExtra(Intent.EXTRA_TEXT, "Build lasting habits with Streak Habit: [App Play Store Link] ") // TODO: Replace with actual Play Store link
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Share Streak Habit via")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(chooserIntent)
    }

    fun openAboutUs() {
        val url = "https://www.example.com/about" // TODO: Replace with actual about us URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }
}