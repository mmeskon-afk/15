package com.example.ai_google_15.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.ai_google_15.ui.theme.Dimens
import com.example.ai_google_15.ui.theme.LightColorScheme

private const val TAG = "AppNavigation"

/**
 * Адаптивная навигация
 * - Оптимизирована для всех размеров экранов
 * - Поддерживает все типы устройств
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightColorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("menu") {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300)) + expandHorizontally(),
                    exit = fadeOut(tween(300)) + shrinkHorizontally()
                ) {
                    MainMenuScreen(onStartGame = { diff ->
                        Log.i(TAG, "Menu: starting game with difficulty=$diff")
                        navController.navigate("game/$diff")
                    })
                }
            }
            composable(
                route = "game/{difficulty}",
                arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
            ) { backStack ->
                val diff = backStack.arguments?.getString("difficulty") ?: "Средний"
                Log.d(TAG, "NavHost: displaying game screen, difficulty=$diff")
                GamePlayScreen(
                    difficulty = diff,
                    onGameFinished = { moves, ai ->
                        Log.i(TAG, "Game finished: moves=$moves, solvedByAI=$ai")
                        navController.navigate("result/$moves/$ai/$diff") { popUpTo("menu") }
                    }
                )
            }
            composable(
                route = "result/{moves}/{solvedByAI}/{difficulty}",
                arguments = listOf(
                    navArgument("moves") { type = NavType.IntType },
                    navArgument("solvedByAI") { type = NavType.BoolType },
                    navArgument("difficulty") { type = NavType.StringType }
                )
            ) { backStack ->
                val moves = backStack.arguments?.getInt("moves") ?: 0
                val solvedByAI = backStack.arguments?.getBoolean("solvedByAI") ?: false
                val diff = backStack.arguments?.getString("difficulty") ?: "Средний"
                Log.d(TAG, "NavHost: displaying result screen, moves=$moves, solvedByAI=$solvedByAI, difficulty=$diff")
                ResultScreen(
                    moves = moves,
                    solvedByAI = solvedByAI,
                    difficulty = diff,
                    onPlayAgain = {
                        Log.i(TAG, "Result: play again clicked")
                        navController.navigate("menu") { popUpTo("menu") { inclusive = true } }
                    }
                )
            }
        }
    }
}
