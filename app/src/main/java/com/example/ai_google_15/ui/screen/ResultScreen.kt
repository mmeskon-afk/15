package com.example.ai_google_15.ui.screen

import android.content.res.Configuration
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

@Composable
fun ResultScreen(moves: Int, solvedByAI: Boolean, difficulty: String, onPlayAgain: () -> Unit) {
    val starsCount = when {
        moves <= 30 -> 5
        moves <= 55 -> 4
        moves <= 85 -> 3
        moves <= 120 -> 2
        else -> 1
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
                .weight(0.8f)
                .padding(horizontal = Dimens.PaddingM),
            contentAlignment = Alignment.Center
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
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
                        modifier = Modifier.weight(1.2f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(bottom = Dimens.PaddingS),
                            shape = RoundedCornerShape(Dimens.CornerMedium),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(Dimens.PaddingL)) {
                                Text(
                                    text = stringResource(R.string.difficulty_label, difficulty),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(Dimens.PaddingXS))
                                Text(
                                    text = stringResource(R.string.moves_performed, moves),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(Dimens.PaddingXS))
                                val buildTypeText = if (solvedByAI) stringResource(R.string.solved_with_ai) else stringResource(R.string.solved_self)
                                Text(
                                    text = stringResource(R.string.puzzle_solved_prefix) + buildTypeText,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Button(
                            onClick = onPlayAgain,
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
                }
            } else {
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

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(bottom = Dimens.PaddingXXL),
                        shape = RoundedCornerShape(Dimens.CornerMedium),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.PaddingL)) {
                            Text(
                                text = stringResource(R.string.difficulty_label, difficulty),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(Dimens.PaddingM))
                            Text(
                                text = stringResource(R.string.moves_performed, moves),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(Dimens.PaddingM))
                            val buildTypeText = if (solvedByAI) stringResource(R.string.solved_with_ai) else stringResource(R.string.solved_self)
                            Text(
                                text = stringResource(R.string.puzzle_solved_prefix) + " " + buildTypeText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Button(
                        onClick = onPlayAgain,
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
