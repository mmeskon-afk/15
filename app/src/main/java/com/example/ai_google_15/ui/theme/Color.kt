package com.example.ai_google_15.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// ========== ДИЗАЙН-ПАЛИТРА ================
// ==========================================

// Фоны
val BackgroundScreen = Color(0xFFF5F7FA)
val GridBackground = Color(0xFFE5E0D5)
val EmptyCell = Color(0xFFF0EDE5)

// Плитки
val TileNormal = Color(0xFF3F51B5)
val TileCorrect = Color(0xFF2196F3)
val TextOnTile = Color(0xFFFFFFFF)
val TileGold = Color(0xFFFFC107)
val TextOnGold = Color(0xFF1A1A2E)

// Кнопки (единый акцент)
val AccentColor = Color(0xFF2196F3)
val AccentColorDark = Color(0xFF1976D2)

// Текст
val TitleText = Color(0xFF1A1A2E)
val BodyText = Color(0xFF1A1A2E)
val SubtleText = Color(0xFF6C757D)

// Для компонентов
val TextPrimary = Color(0xFF1A1A2E)
val TextSecondary = Color(0xFF6C757D)

// Звёзды
val StarGold = Color(0xFFFBC02D)
val StarEmpty = Color(0xFF9E9E9E)

// Сложность (для карточек)
val DifficultyEasy = Color(0xFF4CAF50)
val DifficultyMedium = Color(0xFFFF9800)
val DifficultyHard = Color(0xFFE91E63)

// ==========================================
// ========== ДИЗАЙН-ТОКЕНЫ ================
// ==========================================

object Dimens {
    // Скругления - для более чистого внешнего вида
    val CornerSmall = 16.dp
    val CornerMedium = 20.dp
    val CornerLarge = 24.dp
    val CornerXLarge = 32.dp

    // Отступы - для лучшего баланса
    val PaddingXS = 8.dp
    val PaddingS = 12.dp
    val PaddingM = 16.dp
    val PaddingL = 24.dp
    val PaddingXL = 32.dp
    val PaddingXXL = 40.dp
    val PaddingXXXL = 48.dp

    // Высоты кнопок
    val ButtonHeight = 56.dp

    // Размеры элементов
    val CardNumberSize = 56.dp
    val TilePadding = 8.dp
    val BoardPadding = 12.dp
    val TileTextSize = 32.sp
    val CardDifficultyTextSize = 20.sp
    val CardCheckmarkSize = 24.sp
    val TextMedium = 14.sp
}

// ==========================================
// ========== СХЕМА ЦВЕТОВ MATERIAL 3 =======
// ==========================================

val LightColorScheme = lightColorScheme(
    primaryContainer = TileCorrect,
    onPrimaryContainer = TextOnTile,
    background = BackgroundScreen,
    onBackground = BodyText,
    surface = BackgroundScreen,
    onSurface = BodyText,
    surfaceVariant = GridBackground,
    onSurfaceVariant = BodyText,
    secondaryContainer = TileNormal,
    onSecondaryContainer = TextOnTile,
    onSecondary = BodyText,
    tertiary = TileCorrect,
    onTertiary = TextOnTile
)
