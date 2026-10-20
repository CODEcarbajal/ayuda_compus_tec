package com.carbajal.ayudacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00ACC1),
    secondary = Color(0xFF8E24AA),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color(0xFFF3F4F6),
    onSurface = Color(0xFFF3F4F6),
    surfaceContainer = Color(0xFF282828),
    surfaceContainerLow = Color(0xFF222222),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF374151)
)

private val LightColorScheme = lightColorScheme(
    primary = ColorCliente,
    secondary = ColorSoporte,
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onBackground = Color(0xFF111827),
    onSurface = Color(0xFF111827),
    surfaceContainer = Color(0xFFF3F4F6),
    surfaceContainerLow = Color(0xFFFAFAFA),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE5E7EB)
)

@Composable
fun AyudaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
