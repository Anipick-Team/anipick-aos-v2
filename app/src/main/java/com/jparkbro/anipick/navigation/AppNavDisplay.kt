package com.jparkbro.anipick.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import com.jparkbro.auth.impl.navigation.authEntry
import com.jparkbro.explore.impl.navigation.exploreEntry
import com.jparkbro.home.impl.navigation.homeEntry
import com.jparkbro.mypage.impl.navigation.myPageEntry
import com.jparkbro.ranking.impl.navigation.rankingEntry
import com.jparkbro.splash.api.SplashNavKey
import com.jparkbro.splash.impl.navigation.splashEntry
import kr.agromarket.at.core.navigation.NavigationState
import kr.agromarket.at.core.navigation.Navigator
import kr.agromarket.at.core.navigation.toEntries

private const val FADE_DURATION_MILLIS = 600
private const val SLIDE_DURATION_MILLIS = 250

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavDisplay(
    bottomNavigation: @Composable () -> Unit,
    navigationState: NavigationState,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    SharedTransitionLayout(modifier = modifier) {
        val entryProvider = entryProvider<NavKey> {
            splashEntry(navigator, sharedTransitionScope = this@SharedTransitionLayout)
            authEntry(navigator, sharedTransitionScope = this@SharedTransitionLayout)
            homeEntry(navigator, bottomNavigation)
            rankingEntry(navigator, bottomNavigation)
            exploreEntry(navigator, bottomNavigation)
            myPageEntry(navigator, bottomNavigation)
        }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            sharedTransitionScope = this@SharedTransitionLayout,
            transitionSpec = {
                // Splash에서 나가는 전환(Splash->Login, 자동 로그인 성공 시 Splash->Home)은
                // 슬라이드 없이 기존 그대로 fade만 유지한다.
                if (isLeavingSplash()) {
                    fadeIn(tween(FADE_DURATION_MILLIS)) togetherWith fadeOut(tween(FADE_DURATION_MILLIS))
                } else {
                    slideIntoContainer(SlideDirection.Left, tween(SLIDE_DURATION_MILLIS)) togetherWith
                        slideOutOfContainer(SlideDirection.Left, tween(SLIDE_DURATION_MILLIS))
                }
            },
            popTransitionSpec = {
                slideIntoContainer(SlideDirection.Right, tween(SLIDE_DURATION_MILLIS)) togetherWith
                    slideOutOfContainer(SlideDirection.Right, tween(SLIDE_DURATION_MILLIS))
            },
        )
    }
}

private fun AnimatedContentTransitionScope<Scene<NavKey>>.isLeavingSplash(): Boolean {
    return initialState.entries.lastOrNull()?.contentKey == SplashNavKey.Splash.toString()
}