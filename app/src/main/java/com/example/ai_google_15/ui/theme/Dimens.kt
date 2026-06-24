package com.example.ai_google_15.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Централизованные размеры для адаптивного UI
 * Используются пиксельные эквиваленты для более точного контроля
 */
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

    // Адаптивные размеры ( будут использоваться в UI)
    val MaxBoardSize = 360.dp
    val MinBoardSize = 280.dp
    val BannerHeight = 64.dp

    // Соотношения для адаптивности
    val BoardAspectRatio = 1f // 1:1 для квадратного поля
}
