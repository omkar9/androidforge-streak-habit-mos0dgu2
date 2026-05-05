package com.androidforge.streakhappit.presentation.ui.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidforge.streakhappit.R
import com.androidforge.streakhappit.core.common.Constants
import com.androidforge.streakhappit.core.extensions.toFormattedDateString
import com.androidforge.streakhappit.core.ui.components.ErrorScreen
import com.androidforge.streakhappit.core.ui.components.ShimmerPlaceholder
import com.androidforge.streakhappit.domain.model.CompletionStatus
import com.androidforge.streakhappit.domain.model.HabitWithLogsAndStreaks
import com.androidforge.streakhappit.presentation.theme.error
import com.androidforge.streakhappit.presentation.theme.secondary
import com.androidforge.streakhappit.presentation.theme.success
import com.androidforge.streakhappit.presentation.ui.common.UiState
import com.androidforge.streakhappit.presentation.ui.common.components.StreakCounter
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailsScreen(
    habitId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEditHabit: (Long) -> Unit,
    viewModel: HabitDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(habitId) {
        viewModel.loadHabitDetails(habitId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.habit_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_content_description))
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEditHabit(habitId) }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_habit_content_description))
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
            label = "HabitDetailsContentAnimation"
        ) {\ targetState ->
            when (targetState) {
                is HabitDetailsUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(Modifier.height(16.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(80.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(100.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(200.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(150.dp))
                    }
                }
                is HabitDetailsUiState.Error -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = targetState.message,
                        onRetry = { viewModel.loadHabitDetails(habitId) }
                    )
                }
                is HabitDetailsUiState.Empty -> {
                    // This state should ideally not be reached if habitId is valid, but handle for robustness
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = stringResource(R.string.habit_not_found_message),
                        onRetry = { onNavigateBack() } // No habit to retry loading, go back
                    )
                }
                is HabitDetailsUiState.Offline -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = stringResource(R.string.offline_message),
                        onRetry = { viewModel.loadHabitDetails(habitId) },
                        icon = Icons.Default.Info
                    )
                }
                is HabitDetailsUiState.Success -> {
                    val habitWithLogsAndStreaks = targetState.data
                    HabitDetailsContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState()),
                        habit = habitWithLogsAndStreaks
                    )
                }
            }
        }
    }
}

@Composable
fun HabitDetailsContent(
    modifier: Modifier = Modifier,
    habit: HabitWithLogsAndStreaks
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Habit Name and Description
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.medium, // Using card shape for rounded corners
            border = BorderStroke(1.dp, secondary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = habit.habit.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = habit.habit.description.ifEmpty { stringResource(R.string.no_description_provided) },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Streaks and Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, secondary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.streaks_statistics_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StreakCounter(label = stringResource(R.string.current_streak_label), count = habit.currentStreak)
                    StreakCounter(label = stringResource(R.string.best_streak_label), count = habit.bestStreak)
                    StreakCounter(label = stringResource(R.string.total_completions_label), count = habit.totalCompletions)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.completion_rate_label, String.format("%.1f", habit.completionRate)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar View Placeholder (or simplified view)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, secondary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.habit_history_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Simplified Calendar/Log View
                HabitLogGrid(habitLogs = habit.logs.sortedByDescending { it.date }.take(30)) // Show last 30 days
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun HabitLogGrid(habitLogs: List<com.androidforge.streakhappit.domain.model.HabitLog>) {
    val today = LocalDate.now()
    val datesToShow = (0 until 30).map { today.minusDays(it.toLong()) }.reversed() // Last 30 days

    LazyVerticalGrid(columns = GridCells.Fixed(7),
        contentPadding = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth().height(250.dp) // Fixed height for grid
    ) {
        items(datesToShow, key = { it.toEpochDay() }) { date ->
            val logForDate = habitLogs.find { it.date.isEqual(date) }
            val status = logForDate?.completionStatus ?: CompletionStatus.Missed // Default to missed if no log
            val displayDate = date.dayOfMonth.toString()

            DayLogCell(date = displayDate, status = status, isToday = date.isEqual(today))
        }
    }
}

@Composable
fun DayLogCell(date: String, status: CompletionStatus, isToday: Boolean) {
    val backgroundColor = when (status) {
        CompletionStatus.Completed -> success.copy(alpha = 0.2f)
        CompletionStatus.Skipped -> secondary.copy(alpha = 0.2f)
        CompletionStatus.Missed -> error.copy(alpha = 0.2f)
    }
    val borderColor = when (status) {
        CompletionStatus.Completed -> success
        CompletionStatus.Skipped -> secondary
        CompletionStatus.Missed -> error
    }
    val icon = when (status) {
        CompletionStatus.Completed -> Icons.Default.CheckCircle
        CompletionStatus.Skipped -> Icons.Default.RemoveCircle
        CompletionStatus.Missed -> Icons.Default.Close
    }
    val contentDescription = when (status) {
        CompletionStatus.Completed -> stringResource(R.string.completed_status_description)
        CompletionStatus.Skipped -> stringResource(R.string.skipped_status_description)
        CompletionStatus.Missed -> stringResource(R.string.missed_status_description)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(MaterialTheme.shapes.small) // Use small for chip-like cells
            .background(backgroundColor)
            .border(1.dp, if (isToday) MaterialTheme.colorScheme.primary else borderColor, MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = borderColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}