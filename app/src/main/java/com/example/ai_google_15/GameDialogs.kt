package com.example.ai_google_15

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun VictoryDialog(
    isVictory: Boolean,
    solvedByAI: Boolean,
    onDismiss: () -> Unit
) {
    if (!isVictory) return

    // Выносим логику выбора строк в понятные переменные перед отрисовкой
    val titleRes = if (solvedByAI) R.string.victory_ai_title else R.string.victory_user_title
    val messageRes = if (solvedByAI) R.string.victory_ai_message else R.string.victory_user_message

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.dialog_ok))
            }
        },
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(messageRes),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}
