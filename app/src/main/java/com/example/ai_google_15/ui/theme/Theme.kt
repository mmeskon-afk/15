package com.example.ai_google_15.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OakLight,        // Цвет плиток
    onPrimary = textTiles,
    secondary = OakBog, // Цвет кнопки
    onSecondary = BlackText,
    background = BeigeBackground,
    surface = EmptyGray
)

@Composable
fun AI_google_15Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}