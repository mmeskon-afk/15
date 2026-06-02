package com.example.ai_google_15.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ==========================================
// ========== ИСХОДНАЯ ДИЗАЙН-ПАЛИТРА ======
// ==========================================

val BackgroundScreen = Color(0xFFF9F6F0)   // Основной фон экрана
val GridBackground = Color(0xFFE5E0D5)    // Фон под плитками (сетка)
val TileNormal = Color(0xFF8E9A7F)        // Цвет обычных плиток
val TileCorrect = Color(0xFF7CB342)       // Плитка на правильном месте
val EmptyCell = Color(0xFFF0EDE5)         // Пустая ячейка
val TextOnTile = Color(0xFFFFFFFF)        // Текст на плитках (белый)

// Цвета кнопок
val ShuffleButtonColor = Color(0xFFFFF59D)   // Цвет кнопки "перемешать"
val AiButtonColor = Color(0xFFAED581)        // Цвет кнопки "запустить ИИ"

// Дополнительные цвета элементов
val ButtonText = Color(0xFF000000)           // ТЕКСТ НА КНОПКАХ СТАЛ ЧЕРНЫМ
val TitleText = Color(0xFF3A6B8C)            // Название игры (заголовок)

// ========== УСТАРЕВШИЕ ЦВЕТА (для совместимости) ==========
val BeigeBackground = Color(0xFFFADCAF)
val EmptyGray = Color(0xFFD3D3D3)
val BlackText = Color(0xFF000000)
val OakBog = Color(0xFF3F3F3F)
val textTiles = Color(0xFFFFFFFF)
val square = Color(0xFFEF9A9A)

val TileGold = Color(0xFFFFD700)      // Золотой цвет для анимации победы
val TextOnGold = Color(0xFF3E2723)    // Темно-коричневый текст для контраста на золотом


// ========== СХЕМА ЦВЕТОВ MATERIAL 3 =======
val GameLightColorScheme = lightColorScheme(
    surface = BackgroundScreen,
    surfaceVariant = GridBackground,
    secondaryContainer = TileNormal,
    onSecondaryContainer = TextOnTile,
    primaryContainer = TileCorrect,
    onPrimaryContainer = TextOnTile,
    onSecondary = ButtonText,
    onSurface = TitleText,
    tertiary = TileCorrect,
    onTertiary = TextOnTile
)
