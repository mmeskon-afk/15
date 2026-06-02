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
import androidx.compose.ui.unit.sp
import com.example.ai_google_15.ui.theme.AiButtonColor
import com.example.ai_google_15.ui.theme.TitleText

@Composable
fun MainMenuScreen(onStartGame: (String) -> Unit) {
    // Подтягиваем переводы уровней сложности из strings.xml
    val easyStr = stringResource(R.string.difficulty_easy)
    val mediumStr = stringResource(R.string.difficulty_medium)
    val hardStr = stringResource(R.string.difficulty_hard)

    // Пересоздаем список сложностей на основе локализованных строк
    val difficulties = remember(easyStr, mediumStr, hardStr) {
        listOf(easyStr, mediumStr, hardStr)
    }

    // По умолчанию выбираем "Средний" (или "Medium" в английской локали)
    var selectedDifficulty by remember(mediumStr) { mutableStateOf(mediumStr) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.game_title),
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.choose_difficulty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )

                        difficulties.forEach { diff ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(vertical = 1.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedDifficulty == diff),
                                    onClick = { selectedDifficulty = diff },
                                    colors = RadioButtonDefaults.colors(selectedColor = TitleText)
                                )
                                Text(
                                    text = diff,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onStartGame(selectedDifficulty) },
                            modifier = Modifier.fillMaxWidth(0.8f).height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AiButtonColor, contentColor = Color.Black)
                        ) {
                            Text(text = stringResource(R.string.start_game_button), style = MaterialTheme.typography.bodyLarge)
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
                        text = stringResource(R.string.game_title),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Text(
                        text = stringResource(R.string.choose_difficulty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    difficulties.forEach { diff ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedDifficulty == diff),
                                onClick = { selectedDifficulty = diff },
                                colors = RadioButtonDefaults.colors(selectedColor = TitleText)
                            )
                            Text(
                                text = diff,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onStartGame(selectedDifficulty) },
                        modifier = Modifier.fillMaxWidth(0.5f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AiButtonColor, contentColor = Color.Black)
                    ) {
                        Text(text = stringResource(R.string.start_game_button), style = MaterialTheme.typography.bodyLarge)
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
