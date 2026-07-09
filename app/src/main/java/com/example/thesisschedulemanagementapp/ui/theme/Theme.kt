package com.example.thesisschedulemanagementapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1E5B4F),
    secondary = Color(0xFF5B5F24),
    tertiary = Color(0xFF7A3F2A),
    background = Color(0xFFF8FAF8),
    surface = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8DD7C8),
    secondary = Color(0xFFD1D589),
    tertiary = Color(0xFFE7A58D)
)

@Composable
fun ThesisScheduleManagementTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
