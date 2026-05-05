package com.androidforge.streakhappit.presentation.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.androidforge.streakhappit.R
import com.androidforge.streakhappit.domain.model.Habit
import com.androidforge.streakhappit.presentation.theme.secondary
import com.androidforge.streakhappit.presentation.theme.success

@Composable
fun HabitCard(
    habit: Habit,
    isCompletedToday: Boolean,
    onToggleCompletion: (Long) -> Unit,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val checkColor by animateColorAsState(
        targetValue = if (isCompletedToday) success else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        animationSpec = spring(), label = "CheckColorAnimation"
    )

    Card(
        modifier = modifier
            .clickable(onClick = { onClick(habit.id) })
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium, // Using card shape for rounded corners
        border = BorderStroke(1.dp, secondary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Habit Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = habit.description.ifEmpty { stringResource(R.string.no_description_card) },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Display current streak
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.current_streak_icon_description),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.current_streak_label_short, habit.currentStreak), // Assuming currentStreak is part of Habit for dashboard
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Completion Checkbox
            val completionStateDescription = if (isCompletedToday) stringResource(R.string.habit_completed) else stringResource(R.string.habit_not_completed)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // No ripple for this specific action as it's a toggle
                        onClick = { onToggleCompletion(habit.id) },
                        role = Role.Checkbox
                    )
                    .semantics { stateDescription = completionStateDescription },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCompletedToday) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = stringResource(R.string.toggle_completion_content_description, habit.name),
                    tint = checkColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}