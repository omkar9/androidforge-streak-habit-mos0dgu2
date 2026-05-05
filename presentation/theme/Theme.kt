package com.androidforge.streakhappit.presentation.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.androidforge.streakhappit.core.common.Constants

// Light Color Scheme based on Material3 defaults and designer input
private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
)

// Dark Color Scheme based on Material3 defaults and designer input
private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
)

@Composable
fun StreakHabitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available only on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val targetColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Animate color scheme transitions
    val animatedColorScheme = remember(targetColorScheme) {
        object : ColorScheme {
            override val primary: Color get() = animateColorAsState(targetColorScheme.primary, tween(Constants.ANIMATION_DURATION)).value
            override val onPrimary: Color get() = animateColorAsState(targetColorScheme.onPrimary, tween(Constants.ANIMATION_DURATION)).value
            override val primaryContainer: Color get() = animateColorAsState(targetColorScheme.primaryContainer, tween(Constants.ANIMATION_DURATION)).value
            override val onPrimaryContainer: Color get() = animateColorAsState(targetColorScheme.onPrimaryContainer, tween(Constants.ANIMATION_DURATION)).value
            override val inversePrimary: Color get() = animateColorAsState(targetColorScheme.inversePrimary, tween(Constants.ANIMATION_DURATION)).value
            override val secondary: Color get() = animateColorAsState(targetColorScheme.secondary, tween(Constants.ANIMATION_DURATION)).value
            override val onSecondary: Color get() = animateColorAsState(targetColorScheme.onSecondary, tween(Constants.ANIMATION_DURATION)).value
            override val secondaryContainer: Color get() = animateColorAsState(targetColorScheme.secondaryContainer, tween(Constants.ANIMATION_DURATION)).value
            override val onSecondaryContainer: Color get() = animateColorAsState(targetColorScheme.onSecondaryContainer, tween(Constants.ANIMATION_DURATION)).value
            override val tertiary: Color get() = animateColorAsState(targetColorScheme.tertiary, tween(Constants.ANIMATION_DURATION)).value
            override val onTertiary: Color get() = animateColorAsState(targetColorScheme.onTertiary, tween(Constants.ANIMATION_DURATION)).value
            override val tertiaryContainer: Color get() = animateColorAsState(targetColorScheme.tertiaryContainer, tween(Constants.ANIMATION_DURATION)).value
            override val onTertiaryContainer: Color get() = animateColorAsState(targetColorScheme.onTertiaryContainer, tween(Constants.ANIMATION_DURATION)).value
            override val background: Color get() = animateColorAsState(targetColorScheme.background, tween(Constants.ANIMATION_DURATION)).value
            override val onBackground: Color get() = animateColorAsState(targetColorScheme.onBackground, tween(Constants.ANIMATION_DURATION)).value
            override val surface: Color get() = animateColorAsState(targetColorScheme.surface, tween(Constants.ANIMATION_DURATION)).value
            override val onSurface: Color get() = animateColorAsState(targetColorScheme.onSurface, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceVariant: Color get() = animateColorAsState(targetColorScheme.surfaceVariant, tween(Constants.ANIMATION_DURATION)).value
            override val onSurfaceVariant: Color get() = animateColorAsState(targetColorScheme.onSurfaceVariant, tween(Constants.ANIMATION_DURATION)).value
            override val inverseSurface: Color get() = animateColorAsState(targetColorScheme.inverseSurface, tween(Constants.ANIMATION_DURATION)).value
            override val inverseOnSurface: Color get() = animateColorAsState(targetColorScheme.inverseOnSurface, tween(Constants.ANIMATION_DURATION)).value
            override val error: Color get() = animateColorAsState(targetColorScheme.error, tween(Constants.ANIMATION_DURATION)).value
            override val onError: Color get() = animateColorAsState(targetColorScheme.onError, tween(Constants.ANIMATION_DURATION)).value
            override val errorContainer: Color get() = animateColorAsState(targetColorScheme.errorContainer, tween(Constants.ANIMATION_DURATION)).value
            override val onErrorContainer: Color get() = animateColorAsState(targetColorScheme.onErrorContainer, tween(Constants.ANIMATION_DURATION)).value
            override val outline: Color get() = animateColorAsState(targetColorScheme.outline, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceBright: Color get() = animateColorAsState(targetColorScheme.surfaceBright, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceContainer: Color get() = animateColorAsState(targetColorScheme.surfaceContainer, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceContainerHigh: Color get() = animateColorAsState(targetColorScheme.surfaceContainerHigh, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceContainerHighest: Color get() = animateColorAsState(targetColorScheme.surfaceContainerHighest, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceContainerLow: Color get() = animateColorAsState(targetColorScheme.surfaceContainerLow, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceContainerLowest: Color get() = animateColorAsState(targetColorScheme.surfaceContainerLowest, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceDim: Color get() = animateColorAsState(targetColorScheme.surfaceDim, tween(Constants.ANIMATION_DURATION)).value
            override val surfaceTint: Color get() = animateColorAsState(targetColorScheme.surfaceTint, tween(Constants.ANIMATION_DURATION)).value
            // Custom colors (add them to ColorScheme for full animation if they are used widely)
            val success: Color get() = animateColorAsState(if (darkTheme) md_theme_dark_success else md_theme_light_success, tween(Constants.ANIMATION_DURATION)).value
            val warning: Color get() = animateColorAsState(if (darkTheme) md_theme_dark_warning else md_theme_light_warning, tween(Constants.ANIMATION_DURATION)).value
        }
    }

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}