package com.example.thesisschedulemanagementapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,

    secondary = Secondary,
    secondaryContainer = SecondaryContainer,

    background = Background,
    surface = Surface,

    onBackground = TextPrimary,
    onSurface = TextPrimary,

    error = ErrorColor
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    secondary = Secondary,

    background = DarkBackground,
    surface = DarkSurface,

    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,

    error = ErrorColor
)

@Composable
fun ThesisScheduleManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}