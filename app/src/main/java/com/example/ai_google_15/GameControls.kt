package com.example.ai_google_15

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ai_google_15.ui.theme.AiButtonColor

@Composable
fun GameControls(
    isThinking: Boolean,
    onAIHint: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Оставлена только кнопка подсказки ИИ
        Button(
            onClick = onAIHint,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isThinking,
            colors = ButtonDefaults.buttonColors(
                containerColor = AiButtonColor,
                contentColor = Color.Black,
                disabledContainerColor = AiButtonColor,
                disabledContentColor = Color.Black
            )
        ) {
            if (isThinking) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.ai_thinking),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.ai_button),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
