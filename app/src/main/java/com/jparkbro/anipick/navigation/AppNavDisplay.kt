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

private const val FADE_DURATION_MILLIS = 700
private const val SLIDE_DURATION_MILLIS = 300

/**
 * 바텀 네비게이션 탭의 루트 화면들 — 이 화면들끼리 오갈 때만 슬라이드 대신 fade를 쓴다.
 * 문자열을 따로 손으로 관리하지 않고, [BOTTOM_NAV_ITEMS](정확히 이 4개 탭만 담고 있음)의
 * [TopLevelNavItem.contentKey]를 그대로 가져다 쓴다 — 탭이 늘거나 줄면 그 맵만 고치면 된다.
 */
private val TOP_LEVEL_CONTENT_KEYS = BOTTOM_NAV_ITEMS.values.map { it.contentKey }.toSet()

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
                // Splash에서 나가는 전환, 바텀 네비게이션 탭 루트끼리 오가는 전환(홈<->랭킹<->탐색<->마이페이지)은
                // 슬라이드 없이 fade만 쓴다. 그 외(상세, 검색 등 하위 화면 이동)는 기존 슬라이드 그대로.
                if (isFadeTransition()) {
                    fadeIn(tween(FADE_DURATION_MILLIS)) togetherWith fadeOut(tween(FADE_DURATION_MILLIS))
                } else {
                    slideIntoContainer(SlideDirection.Left, tween(SLIDE_DURATION_MILLIS)) togetherWith
                        slideOutOfContainer(SlideDirection.Left, tween(SLIDE_DURATION_MILLIS))
                }
            },
            popTransitionSpec = {
                if (isFadeTransition()) {
                    fadeIn(tween(FADE_DURATION_MILLIS)) togetherWith fadeOut(tween(FADE_DURATION_MILLIS))
                } else {
                    slideIntoContainer(SlideDirection.Right, tween(SLIDE_DURATION_MILLIS)) togetherWith
                        slideOutOfContainer(SlideDirection.Right, tween(SLIDE_DURATION_MILLIS))
                }
            },
        )
    }
}

/**
 * Splash를 떠나는 전환(도착지가 어디든 상관없이 fade)이거나, 바텀 네비게이션 탭 루트끼리 오가는
 * 전환(양쪽 다 탭 루트여야 fade)일 때 true. 두 조건은 판단 기준이 서로 달라서(전자는 출발지만
 * 보고, 후자는 양쪽 다 봐야 함) 하나의 Set 검사로 합칠 수는 없지만, "fade를 써야 하는가"라는
 * 결론 하나로 묶어서 호출부에서는 이 함수 하나만 보면 되게 했다.
 */
private fun AnimatedContentTransitionScope<Scene<NavKey>>.isFadeTransition(): Boolean {
    val fromKey = initialState.entries.lastOrNull()?.contentKey
    val toKey = targetState.entries.lastOrNull()?.contentKey

    val isLeavingSplash = fromKey == TOP_LEVEL_ITEMS.getValue(SplashNavKey.Splash).contentKey
    val isTopLevelSwitch = fromKey in TOP_LEVEL_CONTENT_KEYS && toKey in TOP_LEVEL_CONTENT_KEYS

    return isLeavingSplash || isTopLevelSwitch
}