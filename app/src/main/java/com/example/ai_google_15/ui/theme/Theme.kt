package com.example.ai_google_15.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    // Основной цвет (плитки на своем месте)
    primaryContainer = TileCorrect,
    onPrimaryContainer = TextOnTile,

    // Фоновые параметры экрана приложения
    background = BackgroundScreen,
    onBackground = ButtonText,

    // Фон поверхностей и Scaffold
    surface = BackgroundScreen,
    onSurface = TitleText, // Для заголовков используем специальный TitleText

    // Подложка (рамка) игрового поля 4х4
    surfaceVariant = GridBackground,
    onSurfaceVariant = ButtonText,

    // Обычные плитки и текст на них
    secondaryContainer = TileNormal,
    onSecondaryContainer = TextOnTile,

    // Цвет текста и индикаторов для кастомных кнопок
    onSecondary = ButtonText,

    // Дополнительные системные цвета
    tertiary = TileCorrect,
    onTertiary = TextOnTile
)

@Composable
fun AI_google_15Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
