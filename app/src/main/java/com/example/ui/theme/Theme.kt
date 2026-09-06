package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = TextWhite,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = PurpleVibrant,
    secondary = PurpleVibrant,
    onSecondary = PitchBlack,
    secondaryContainer = PurpleDark,
    onSecondaryContainer = PurpleGlow,
    tertiary = PurpleVibrant,
    background = PitchBlack,
    onBackground = TextWhite,
    surface = DeepCharcoal,
    onSurface = TextWhite,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextMuted,
    outline = SurfaceBorder,
    outlineVariant = SurfaceBorder
)

@Composable
fun Good7Theme(
    content: @Composable () -> Unit
) {
    // Strictly dark theme as per user design requirements (black, purple, white)
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
