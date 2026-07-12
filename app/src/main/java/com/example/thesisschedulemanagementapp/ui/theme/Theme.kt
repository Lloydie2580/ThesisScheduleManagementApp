package com.example.thesisschedulemanagementapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1E5B4F),
    secondary = Color(0xFF5B5F24),
    tertiary = Color(0xFF7A3F2A),
    background = Color(0xFFF8FAF8),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF191C1B),
    onSurface = Color(0xFF191C1B)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8DD7C8),
    secondary = Color(0xFFD1D589),
    tertiary = Color(0xFFE7A58D),
    background = Color(0xFF1C1C1C),
    surface = Color(0xFF252525),
    onPrimary = Color(0xFF00372F),
    onSecondary = Color(0xFF333300),
    onTertiary = Color(0xFF4C1D05),
    onBackground = Color(0xFFE1E3E1),
    onSurface = Color(0xFFE1E3E1)
)

@Composable
fun ThesisScheduleManagementTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
