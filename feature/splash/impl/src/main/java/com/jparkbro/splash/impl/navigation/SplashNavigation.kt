package com.jparkbro.splash.impl.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.auth.api.navigateToLogin
import com.jparkbro.home.api.navigateToHomeMain
import com.jparkbro.splash.api.SplashNavKey
import com.jparkbro.splash.impl.SplashRoot
import kr.agromarket.at.core.navigation.Navigator

/** [SplashNavKey.Splash]의 contentKey — 다른 모듈(app의 탭 전환 애니메이션 판단 등)에서도 참조할 수 있게 공개. */
const val SPLASH_CONTENT_KEY = "SplashNavKey.Splash"

@OptIn(ExperimentalSharedTransitionApi::class)
fun EntryProviderScope<NavKey>.splashEntry(
    navigator: Navigator,
    sharedTransitionScope: SharedTransitionScope,
) {
    entry<SplashNavKey.Splash>(clazzContentKey = { SPLASH_CONTENT_KEY }) {
        SplashRoot(
            onNavigateToHome = navigator::navigateToHomeMain,
            onNavigateToLogin = navigator::navigateToLogin,
            sharedTransitionScope = sharedTransitionScope,
        )
    }
}
