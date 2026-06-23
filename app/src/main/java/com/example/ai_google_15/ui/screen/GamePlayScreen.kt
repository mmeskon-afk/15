package com.example.ai_google_15.ui.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun GamePlayScreen(
    difficulty: String,
    onGameFinished: (Int, Boolean) -> Unit
) {
    val viewModel: GameViewModel = viewModel()

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

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                        .padding(Dimens.PaddingXS),
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
                        Spacer(modifier = Modifier.height(Dimens.PaddingXS))
                        Text(
                            text = stringResource(R.string.moves_count, viewModel.moveCount),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingS))
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
                        .padding(Dimens.PaddingL),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.difficulty_label, difficulty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = Dimens.PaddingXS)
                    )

                    Text(
                        text = stringResource(R.string.moves_count, viewModel.moveCount),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = Dimens.PaddingM)
                    )

                    GameBoard(
                        tiles = viewModel.tiles,
                        isThinking = viewModel.isThinking,
                        isVictory = viewModel.isVictory,
                        goldTilesCount = viewModel.goldTilesCount,
                        onTileClick = {},
                        onSwipe = { viewModel.handleSwipe(it) }
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingM))

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
