package com.example.ai_google_15.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
