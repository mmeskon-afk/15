package com.example.ai_google_15.ui.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai_google_15.R
import com.example.ai_google_15.ui.component.GameBoard
import com.example.ai_google_15.ui.component.GameControls
import com.example.ai_google_15.ui.component.YandexBannerAd
import com.example.ai_google_15.ui.theme.*
import com.example.ai_google_15.viewmodel.GameViewModel
import kotlinx.coroutines.delay

private const val TAG = "GamePlayScreen"

/**
 * Адаптивный игровой экран
 * - Оптимизирован для всех размеров экранов и соотношений сторон
 * - Поддерживает portrait и landscape режимы
 * - Плавные анимации при изменении ориентации
 */
@Composable
fun GamePlayScreen(
    difficulty: String,
    onGameFinished: (Int, Boolean) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val viewModel: GameViewModel = viewModel()

    // Логическая инициализация
    LaunchedEffect(difficulty) {
        Log.d(TAG, "GamePlayScreen: initializing game with difficulty=$difficulty")
        viewModel.initGame(difficulty)
    }

    LaunchedEffect(viewModel.goldTilesCount) {
        if (viewModel.isVictory && viewModel.goldTilesCount == 15) {
            Log.d(TAG, "GamePlayScreen: victory animation complete, navigating to result")
            delay(500)
            onGameFinished(viewModel.moveCount, viewModel.solvedByAI)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Основной контент
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isLandscape) Dimens.PaddingXS else Dimens.PaddingL)
        ) {
            if (isLandscape) {
                // Landscape: board слева, controls справа
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Игровое поле - занимает 55% ширины
                    Box(
                        modifier = Modifier
                            .weight(0.55f)
                            .fillMaxHeight()
                    ) {
                        GameBoard(
                            tiles = viewModel.tiles,
                            isThinking = viewModel.isThinking,
                            isVictory = viewModel.isVictory,
                            goldTilesCount = viewModel.goldTilesCount,
                            onTileClick = {},
                            onSwipe = { viewModel.handleSwipe(it) }
                        )
                    }

                    // Панель управления - занимает 45% ширины
                    Column(
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InfoCard(
                            title = stringResource(R.string.difficulty_label, difficulty),
                            value = ""
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingS))
                        InfoCard(
                            title = stringResource(R.string.moves_count, viewModel.moveCount),
                            value = ""
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingM))
                        GameControls(
                            isThinking = viewModel.isThinking,
                            onAIHint = { viewModel.performAIMove() }
                        )
                    }
                }
            } else {
                // Portrait: top - info, middle - board, bottom - controls
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Верхняя панель с информацией
                    Box(
                        modifier = Modifier
                            .weight(0.15f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            InfoCard(
                                title = stringResource(R.string.difficulty_label, difficulty),
                                value = ""
                            )
                            Spacer(modifier = Modifier.height(Dimens.PaddingXS))
                            InfoCard(
                                title = stringResource(R.string.moves_count, viewModel.moveCount),
                                value = ""
                            )
                        }
                    }

                    // Игровое поле - занимает 70% высоты
                    Box(
                        modifier = Modifier
                            .weight(0.70f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        GameBoard(
                            tiles = viewModel.tiles,
                            isThinking = viewModel.isThinking,
                            isVictory = viewModel.isVictory,
                            goldTilesCount = viewModel.goldTilesCount,
                            onTileClick = {},
                            onSwipe = { viewModel.handleSwipe(it) }
                        )
                    }

                    // Нижняя панель управления - занимает 15% высоты
                    Box(
                        modifier = Modifier
                            .weight(0.15f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        GameControls(
                            isThinking = viewModel.isThinking,
                            onAIHint = { viewModel.performAIMove() }
                        )
                    }
                }
            }
        }

        // Реклама внизу
        AnimatedVisibility(
            visible = !viewModel.isVictory,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)) + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(64.dp)
                    .padding(bottom = 8.dp)
            ) {
                YandexBannerAd(adUnitId = "R-M-19272453-2", modifier = Modifier.fillMaxWidth())
            }
        }

        // Кнопка сброса в углу
        IconButton(
            onClick = { viewModel.resetGame() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Dimens.PaddingM)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.reset_button),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Адаптивная карточка информации
 * - Плавно анимируется при изменениях
 * - Работает на всех размерах экранов
 */
@Composable
private fun InfoCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(Dimens.PaddingXS),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingM)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.PaddingXS))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
