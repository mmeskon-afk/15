package com.example.ai_google_15

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.compose.Banner
import com.yandex.mobile.ads.compose.BannerSize
import com.yandex.mobile.ads.compose.rememberBannerAdState

@Composable
fun YandexBannerAd(adUnitId: String, modifier: Modifier = Modifier) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val bannerState = rememberBannerAdState(adSize = BannerSize.Sticky(width = screenWidth))

    LaunchedEffect(adUnitId) {
        val adRequest = AdRequest.Builder(adUnitId).build()
        bannerState.loadAd(adRequest)
    }
    Banner(state = bannerState, modifier = modifier.fillMaxWidth())
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MainMenuScreen(onStartGame = { diff -> navController.navigate("game/$diff") })
        }
        composable(
            route = "game/{difficulty}",
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { backStack ->
            val diff = backStack.arguments?.getString("difficulty") ?: "Средний"
            GamePlayScreen(
                difficulty = diff,
                onGameFinished = { moves, ai ->
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
            ResultScreen(
                moves = backStack.arguments?.getInt("moves") ?: 0,
                solvedByAI = backStack.arguments?.getBoolean("solvedByAI") ?: false,
                difficulty = backStack.arguments?.getString("difficulty") ?: "Средний",
                onPlayAgain = { navController.navigate("menu") { popUpTo("menu") { inclusive = true } } }
            )
        }
    }
}
