package com.example.ai_google_15

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_google_15.ui.theme.ShuffleButtonColor

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
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Верхняя зона: 80% под статистику
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(horizontal = 12.dp),
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
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Row(horizontalArrangement = Arrangement.Center) {
                            repeat(5) { index ->
                                Text(
                                    text = if (index < starsCount) "★" else "☆",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                    color = if (index < starsCount) Color(0xFFFBC02D) else Color.LightGray,
                                    modifier = Modifier.padding(horizontal = 2.dp)
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
                            modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 8.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = stringResource(R.string.difficulty_label, difficulty), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp), color = Color.Black)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = stringResource(R.string.moves_performed, moves), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp), color = Color.Black)
                                Spacer(modifier = Modifier.height(2.dp))

                                val buildTypeText = if (solvedByAI) stringResource(R.string.solved_with_ai) else stringResource(R.string.solved_self)
                                Text(
                                    text = stringResource(R.string.puzzle_solved_prefix) + buildTypeText,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                                    color = Color.Black
                                )
                            }
                        }

                        Button(
                            onClick = onPlayAgain,
                            modifier = Modifier.fillMaxWidth(0.9f).height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ShuffleButtonColor, contentColor = Color.Black)
                        ) {
                            Text(text = stringResource(R.string.play_again_button), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
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
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            Text(
                                text = if (index < starsCount) "★" else "☆",
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 44.sp),
                                color = if (index < starsCount) Color(0xFFFBC02D) else Color.LightGray,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 24.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = stringResource(R.string.difficulty_label, difficulty), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = stringResource(R.string.moves_performed, moves), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                            Spacer(modifier = Modifier.height(6.dp))

                            val buildTypeText = if (solvedByAI) stringResource(R.string.solved_with_ai) else stringResource(R.string.solved_self)
                            Text(
                                text = stringResource(R.string.puzzle_solved_prefix) + buildTypeText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                        }
                    }

                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier.fillMaxWidth(0.5f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ShuffleButtonColor, contentColor = Color.Black)
                    ) {
                        Text(text = stringResource(R.string.play_again_button), style = MaterialTheme.typography.bodyLarge)
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
