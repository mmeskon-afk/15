package com.example.ai_google_15.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ai_google_15.R
import com.example.ai_google_15.ui.theme.AccentColor
import com.example.ai_google_15.ui.theme.BodyText
import com.example.ai_google_15.ui.theme.Dimens
import com.example.ai_google_15.ui.theme.TextPrimary
import com.example.ai_google_15.ui.theme.TextSecondary

@Composable
fun GameControls(
    isThinking: Boolean,
    onAIHint: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onAIHint,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .padding(8.dp),
            enabled = !isThinking,
            shape = RoundedCornerShape(Dimens.CornerLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentColor,
                contentColor = BodyText,
                disabledContainerColor = AccentColor.copy(alpha = 0.5f),
                disabledContentColor = BodyText.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            if (isThinking) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = TextPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.ai_thinking),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.ai_button),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }
        }
    }
}
