package com.example.thesisschedulemanagementapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Primary,

    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = Secondary,

    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,

    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,

    outline = Border,
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
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}