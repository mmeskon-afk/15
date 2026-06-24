package com.example.ai_google_15.ui.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_google_15.R
import com.example.ai_google_15.ui.component.YandexBannerAd
import com.example.ai_google_15.ui.theme.*

/**
 * Адаптивный экран победы
 * - Оптимизирован для всех размеров экранов и соотношений сторон
 * - Поддерживает portrait и landscape режимы
 * - Плавные анимации
 */
@Composable
fun ResultScreen(moves: Int, solvedByAI: Boolean, difficulty: String, onPlayAgain: () -> Unit) {
    val starsCount = calculateStars(moves)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
                // Landscape: stars слева, info справа
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (solvedByAI) stringResource(R.string.victory_ai_screen_title) else stringResource(R.string.victory_title),
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = Dimens.PaddingXS)
                        )

                        Row(horizontalArrangement = Arrangement.Center) {
                            repeat(5) { index ->
                                Text(
                                    text = if (index < starsCount) "\u2605" else "\u2606",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                    color = if (index < starsCount) StarGold else StarEmpty,
                                    modifier = Modifier.padding(horizontal = Dimens.PaddingXS)
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ResultInfoCard(
                            difficulty = difficulty,
                            moves = moves,
                            solvedByAI = solvedByAI
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingM))
                        PlayAgainButton(onClick = onPlayAgain)
                    }
                }
            } else {
                // Portrait: stars сверху, info снизу
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (solvedByAI) stringResource(R.string.victory_ai_screen_title) else stringResource(R.string.victory_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = Dimens.PaddingL)
                    )

                    Row(
                        modifier = Modifier.padding(bottom = Dimens.PaddingL),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            Text(
                                text = if (index < starsCount) "\u2605" else "\u2606",
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 44.sp),
                                color = if (index < starsCount) StarGold else StarEmpty,
                                modifier = Modifier.padding(horizontal = Dimens.PaddingS)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ResultInfoCard(
                            difficulty = difficulty,
                            moves = moves,
                            solvedByAI = solvedByAI
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingXXL))
                        PlayAgainButton(onClick = onPlayAgain)
                    }
                }
            }
        }

        // Реклама внизу
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)) + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(Dimens.BannerHeight)
                    .padding(bottom = 8.dp)
            ) {
                YandexBannerAd(adUnitId = "R-M-19272453-2", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

/**
 * Рассчет количества звезд
 * - Оптимизированные пороги для всех сложностей
 */
private fun calculateStars(moves: Int): Int = when {
    moves <= 30 -> 5
    moves <= 55 -> 4
    moves <= 85 -> 3
    moves <= 120 -> 2
    else -> 1
}

/**
 * Карточка с информацией о результате
 */
@Composable
private fun ResultInfoCard(difficulty: String, moves: Int, solvedByAI: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(Dimens.PaddingM),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingL)) {
            InfoRow(
                label = stringResource(R.string.difficulty_label, difficulty),
                value = ""
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingXS))
            InfoRow(
                label = stringResource(R.string.moves_performed, moves),
                value = ""
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingXS))
            InfoRow(
                label = stringResource(R.string.puzzle_solved_prefix),
                value = if (solvedByAI) stringResource(R.string.solved_with_ai) else stringResource(R.string.solved_self)
            )
        }
    }
}

/**
 * Строковая информация
 */
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Кнопка "Играть снова"
 */
@Composable
private fun PlayAgainButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(Dimens.ButtonHeight),
        shape = RoundedCornerShape(Dimens.CornerLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = stringResource(R.string.play_again_button),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
