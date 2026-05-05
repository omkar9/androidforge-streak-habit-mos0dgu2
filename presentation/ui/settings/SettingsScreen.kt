package com.androidforge.streakhappit.presentation.ui.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidforge.streakhappit.R
import com.androidforge.streakhappit.core.common.Constants
import com.androidforge.streakhappit.core.ui.components.ErrorScreen
import com.androidforge.streakhappit.core.ui.components.ShimmerPlaceholder
import com.androidforge.streakhappit.presentation.ui.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_content_description))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn()) togetherWith
                        (slideOutVertically { height -> -height } + fadeOut())
            },
            label = "SettingsContentAnimation"
        ) {\ targetState ->
            when (targetState) {
                is SettingsUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                    ) {
                        Spacer(Modifier.height(16.dp))
                        repeat(4) {
                            ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(60.dp))
                        }
                    }
                }
                is SettingsUiState.Error -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = targetState.message,
                        onRetry = viewModel::loadSettings
                    )
                }
                is SettingsUiState.Empty -> {
                    // Settings screen usually doesn't have an empty state, but for robustness:
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = stringResource(R.string.settings_load_error),
                        onRetry = viewModel::loadSettings
                    )
                }
                is SettingsUiState.Offline -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = stringResource(R.string.offline_message),
                        onRetry = viewModel::loadSettings,
                        icon = Icons.Default.Info
                    )
                }
                is SettingsUiState.Success -> {
                    val settings = targetState.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        SettingItem(
                            title = stringResource(R.string.dark_mode_setting),
                            description = stringResource(R.string.dark_mode_description),
                            icon = Icons.Default.Info
                        ) {
                            Switch(checked = settings.isDarkModeEnabled, onCheckedChange = viewModel::onDarkModeToggled)
                        }
                        SettingItem(
                            title = stringResource(R.string.notifications_setting),
                            description = stringResource(R.string.notifications_description),
                            icon = Icons.Default.Info
                        ) {
                            Switch(checked = settings.areNotificationsEnabled, onCheckedChange = viewModel::onNotificationsToggled)
                        }
                        ClickableSettingItem(
                            title = stringResource(R.string.privacy_policy_setting),
                            description = stringResource(R.string.privacy_policy_description),
                            icon = Icons.Default.PrivacyTip,
                            onClick = { viewModel.openPrivacyPolicy() }
                        )
                        ClickableSettingItem(
                            title = stringResource(R.string.share_app_setting),
                            description = stringResource(R.string.share_app_description),
                            icon = Icons.Default.Share,
                            onClick = { viewModel.shareApp() }
                        )
                        ClickableSettingItem(
                            title = stringResource(R.string.about_us_setting),
                            description = stringResource(R.string.about_us_description),
                            icon = Icons.AutoMirrored.Filled.HelpOutline,
                            onClick = { viewModel.openAboutUs() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Icon is decorative, description handled by text
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        content()
    }
}

@Composable
fun ClickableSettingItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}