package com.example.ai_google_15

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ai_google_15.ui.theme.AI_google_15Theme
import com.yandex.mobile.ads.common.YandexAds
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        YandexAds.initialize(this) {}
        setContent {
            AI_google_15Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Запуск через менеджер экранов
                    AppNavigation()
                }
            }
        }
    }
}