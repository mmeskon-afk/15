package com.example.ai_google_15

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun GamePlayScreen(
    difficulty: String,
    onGameFinished: (Int, Boolean) -> Unit
) {
    val viewModel: GameViewModel = viewModel()

    // Локализованные строки уровней сложности для корректного сопоставления шагов
    val easyStr = stringResource(R.string.difficulty_easy)
    val hardStr = stringResource(R.string.difficulty_hard)

    LaunchedEffect(difficulty) {
        viewModel.initGame(difficulty)
    }

    LaunchedEffect(viewModel.goldTilesCount) {
        if (viewModel.isVictory && viewModel.goldTilesCount == 15) {
            delay(500)
            onGameFinished(viewModel.moveCount, viewModel.solvedByAI)
        }
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Верхняя зона: 80% под весь игровой процесс
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            contentAlignment = Alignment.Center
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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

                    Column(
                        modifier = Modifier.weight(0.9f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.difficulty_label, difficulty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.moves_count, viewModel.moveCount),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        GameControls(
                            isThinking = viewModel.isThinking,
                            onAIHint = { viewModel.performAIMove() }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.difficulty_label, difficulty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = stringResource(R.string.moves_count, viewModel.moveCount),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    GameBoard(
                        tiles = viewModel.tiles,
                        isThinking = viewModel.isThinking,
                        isVictory = viewModel.isVictory,
                        goldTilesCount = viewModel.goldTilesCount,
                        onTileClick = {},
                        onSwipe = { viewModel.handleSwipe(it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    GameControls(
                        isThinking = viewModel.isThinking,
                        onAIHint = { viewModel.performAIMove() }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            contentAlignment = Alignment.BottomCenter
        ) {
            YandexBannerAd(adUnitId = "R-M-19272453-2", modifier = Modifier.fillMaxSize())
        }
    }
}
