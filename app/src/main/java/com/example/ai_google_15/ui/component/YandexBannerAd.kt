package com.example.ai_google_15.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
