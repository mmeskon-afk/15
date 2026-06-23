package com.example.ai_google_15.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ai_google_15.ui.screen.GamePlayScreen
import com.example.ai_google_15.ui.screen.MainMenuScreen
import com.example.ai_google_15.ui.screen.ResultScreen

private const val TAG = "AppNavigation"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            Log.d(TAG, "NavHost: displaying menu screen")
            MainMenuScreen(onStartGame = { diff ->
                Log.i(TAG, "Menu: starting game with difficulty=$diff")
                navController.navigate("game/$diff")
            })
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
