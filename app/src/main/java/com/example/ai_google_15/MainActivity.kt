package com.example.ai_google_15

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ai_google_15.ui.navigation.AppNavigation
import com.example.ai_google_15.ui.theme.AI_google_15Theme
import com.yandex.mobile.ads.common.YandexAds

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: starting app")
        YandexAds.initialize(this) {
            Log.d(TAG, "YandexAds initialized")
        }
        setContent {
            AI_google_15Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Log.d(TAG, "setContent: launching AppNavigation")
                    AppNavigation()
                }
            }
        }
    }
}
