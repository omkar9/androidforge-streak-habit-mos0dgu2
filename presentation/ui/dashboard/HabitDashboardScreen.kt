package com.androidforge.streakhappit.presentation.ui.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidforge.streakhappit.R
import com.androidforge.streakhappit.core.common.Constants
import com.androidforge.streakhappit.core.ui.components.ErrorScreen
import com.androidforge.streakhappit.core.ui.components.ShimmerPlaceholder
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.presentation.theme.primaryGradient
import com.androidforge.streakhappit.presentation.ui.common.UiState
import com.androidforge.streakhappit.presentation.ui.common.components.AdBannerComposable
import com.androidforge.streakhappit.presentation.ui.common.components.HabitCard
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HabitDashboardScreen(
    onNavigateToAddEditHabit: (Long?) -> Unit,
    onNavigateToHabitDetails: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HabitDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings_content_description))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToAddEditHabit(null) },
                containerColor = primaryGradient,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_habit_content_description))
            }
        }
    ) {\ paddingValues ->
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn(tween(Constants.ANIMATION_DURATION))) togetherWith
                        (slideOutVertically { height -> -height } + fadeOut(tween(Constants.ANIMATION_DURATION)))
            },
            label = "DashboardContentAnimation"
        ) {\ targetState ->
            when (targetState) {
                is HabitDashboardUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))
                        repeat(5) {
                            ShimmerPlaceholder(modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp))
                        }
                    }
                }
                is HabitDashboardUiState.Error -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = targetState.message,
                        onRetry = viewModel::loadHabits
                    )
                }
                is HabitDashboardUiState.Empty -> {
                    EmptyDashboardScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        onAddHabit = { onNavigateToAddEditHabit(null) }
                    )
                }
                is HabitDashboardUiState.Offline -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = stringResource(R.string.offline_message),
                        onRetry = viewModel::loadHabits, // Attempt to reload on retry
                        icon = Icons.Default.Info
                    )
                }
                is HabitDashboardUiState.Success -> {
                    val habits = targetState.data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            AdBannerComposable(modifier = Modifier.fillMaxWidth())
                            Spacer(Modifier.height(8.dp))
                        }
                        items(habits, key = { it.habit.id }) {\ habitWithStatus ->
                            HabitCard(
                                habit = habitWithStatus.habit,
                                isCompletedToday = habitWithStatus.isCompletedToday,
                                onToggleCompletion = { habitId ->
                                    viewModel.toggleHabitCompletion(habitId, LocalDate.now())
                                },
                                onClick = { habitId -> onNavigateToHabitDetails(habitId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .animateItemPlacement(tween(durationMillis = Constants.ANIMATION_DURATION))
                            )
                        }
                        item {
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDashboardScreen(modifier: Modifier = Modifier, onAddHabit: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.empty_dashboard_icon_description),
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_dashboard_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.empty_dashboard_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Reusing the primary button style from the design system
        Box(modifier = Modifier
            .fillMaxWidth(0.7f)
            .clickable { onAddHabit() }
            .background(primaryGradient, MaterialTheme.shapes.small)
            .padding(vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.add_first_habit_button),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}