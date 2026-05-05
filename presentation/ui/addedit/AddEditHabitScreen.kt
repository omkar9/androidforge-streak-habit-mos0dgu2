package com.androidforge.streakhappit.presentation.ui.addedit

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidforge.streakhappit.R
import com.androidforge.streakhappit.core.common.Constants
import com.androidforge.streakhappit.core.extensions.toFormattedTimeString
import com.androidforge.streakhappit.core.ui.components.ErrorScreen
import com.androidforge.streakhappit.core.ui.components.ShimmerPlaceholder
import com.androidforge.streakhappit.domain.model.FrequencyType
import com.androidforge.streakhappit.presentation.theme.primaryGradient
import com.androidforge.streakhappit.presentation.theme.secondary
import com.androidforge.streakhappit.presentation.theme.surfaceVariant
import com.androidforge.streakhappit.presentation.ui.common.UiState
import java.time.DayOfWeek
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditHabitScreen(
    habitId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddEditHabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(habitId) {
        if (habitId != -1L) {
            viewModel.loadHabit(habitId!!)
        } else {
            viewModel.resetForm()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AddEditHabitUiState.Success && (uiState as AddEditHabitUiState.Success).habitId != null) {
            Toast.makeText(context, context.getString(R.string.habit_saved_success), Toast.LENGTH_SHORT).show()
            onNavigateBack()
        }
        if (uiState is AddEditHabitUiState.Error) {
            Toast.makeText(context, (uiState as AddEditHabitUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            viewModel.onReminderTimeChanged(LocalTime.of(hourOfDay, minute))
        },
        viewModel.reminderTime.hour,
        viewModel.reminderTime.minute,
        false
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (habitId == -1L) stringResource(R.string.add_habit_title) else stringResource(R.string.edit_habit_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
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
            label = "AddEditHabitContentAnimation"
        ) {\ targetState ->
            when (targetState) {
                is AddEditHabitUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(Modifier.height(16.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(56.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(100.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(150.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(56.dp))
                        ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(56.dp))
                    }
                }
                is AddEditHabitUiState.Error -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = targetState.message,
                        onRetry = { if (habitId != -1L) viewModel.loadHabit(habitId!!) else viewModel.resetForm() }
                    )
                }
                is AddEditHabitUiState.Offline -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        message = stringResource(R.string.offline_message),
                        onRetry = { if (habitId != -1L) viewModel.loadHabit(habitId!!) else viewModel.resetForm() },
                        icon = Icons.Default.Info
                    )
                }
                else -> {
                    // Success, Empty, or initial state (form fields are available)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = viewModel.name,
                            onValueChange = viewModel::onNameChanged,
                            label = { Text(stringResource(R.string.habit_name_label)) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = viewModel.nameError != null,
                            supportingText = { viewModel.nameError?.let { Text(it) } }
                        )

                        OutlinedTextField(
                            value = viewModel.description,
                            onValueChange = viewModel::onDescriptionChanged,
                            label = { Text(stringResource(R.string.habit_description_label)) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5
                        )

                        HabitFrequencySelector(
                            selectedFrequency = viewModel.frequencyType,
                            onFrequencySelected = viewModel::onFrequencyTypeChanged,
                            selectedDays = viewModel.specificDays,
                            onDaySelected = viewModel::onSpecificDayToggled
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.reminder_label),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Switch(
                                checked = viewModel.hasReminder,
                                onCheckedChange = viewModel::onHasReminderChanged
                            )
                        }

                        AnimatedVisibility(visible = viewModel.hasReminder) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.small)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { timePickerDialog.show() }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = stringResource(R.string.time_picker_content_description),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = viewModel.reminderTime.toFormattedTimeString(),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                        }

                        // Placeholder for color/icon selection - could be a custom dialog/bottom sheet
                        Text(
                            text = stringResource(R.string.habit_appearance_label),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                        // TODO: Implement actual color/icon picker
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.color_icon_picker_placeholder),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = viewModel::saveHabit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = viewModel.isFormValid,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(primaryGradient)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AnimatedContent(
                                    targetState = (uiState == AddEditHabitUiState.Saving),
                                    transitionSpec = {
                                        fadeIn() + slideInVertically { it / 2 } togetherWith
                                                fadeOut() + slideOutVertically { -it / 2 }
                                    },
                                    label = "SaveButtonContentAnimation"
                                ) {\ isSaving ->
                                    if (isSaving) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                                    } else {
                                        Text(
                                            text = stringResource(R.string.save_habit_button),
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HabitFrequencySelector(
    selectedFrequency: FrequencyType,
    onFrequencySelected: (FrequencyType) -> Unit,
    selectedDays: Set<DayOfWeek>,
    onDaySelected: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.habit_frequency_label),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FrequencyChip(
                text = stringResource(R.string.frequency_daily),
                isSelected = selectedFrequency == FrequencyType.Daily,
                onClick = { onFrequencySelected(FrequencyType.Daily) },
                modifier = Modifier.weight(1f)
            )
            FrequencyChip(
                text = stringResource(R.string.frequency_weekly),
                isSelected = selectedFrequency == FrequencyType.Weekly,
                onClick = { onFrequencySelected(FrequencyType.Weekly) },
                modifier = Modifier.weight(1f)
            )
            FrequencyChip(
                text = stringResource(R.string.frequency_specific_days),
                isSelected = selectedFrequency == FrequencyType.SpecificDays,
                onClick = { onFrequencySelected(FrequencyType.SpecificDays) },
                modifier = Modifier.weight(1f)
            )
        }

        AnimatedVisibility(visible = selectedFrequency == FrequencyType.SpecificDays) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = stringResource(R.string.select_days_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DayOfWeek.entries.forEach { day ->
                        DayChip(
                            day = day,
                            isSelected = selectedDays.contains(day),
                            onClick = { onDaySelected(day) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FrequencyChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) primaryGradient else Brush.horizontalGradient(listOf(surfaceVariant, surfaceVariant))
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small) // Using small for chip shape
            .background(backgroundColor)
            .clickable(onClick = onClick, interactionSource = remember { MutableInteractionSource() }, indication = null)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )
    }
}

@Composable
fun DayChip(
    day: DayOfWeek,
    isSelected: Boolean,
    onClick: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) primaryGradient else Brush.horizontalGradient(listOf(surfaceVariant, surfaceVariant))
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    val dayAbbreviation = when (day) {
        DayOfWeek.MONDAY -> stringResource(R.string.mon_abbr)
        DayOfWeek.TUESDAY -> stringResource(R.string.tue_abbr)
        DayOfWeek.WEDNESDAY -> stringResource(R.string.wed_abbr)
        DayOfWeek.THURSDAY -> stringResource(R.string.thu_abbr)
        DayOfWeek.FRIDAY -> stringResource(R.string.fri_abbr)
        DayOfWeek.SATURDAY -> stringResource(R.string.sat_abbr)
        DayOfWeek.SUNDAY -> stringResource(R.string.sun_abbr)
    }

    Box(
        modifier = modifier
            .size(40.dp) // Square chip
            .clip(RoundedCornerShape(8.dp)) // Using 8dp for chip shape
            .background(backgroundColor)
            .toggleable(
                value = isSelected,
                onValueChange = { onClick(day) },
                role = Role.Checkbox,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayAbbreviation,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )
    }
}