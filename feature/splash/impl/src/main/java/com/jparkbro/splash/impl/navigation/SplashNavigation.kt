package com.jparkbro.splash.impl.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.auth.api.navigateToLogin
import com.jparkbro.core.navigation.Navigator
import com.jparkbro.home.api.navigateToHomeMain
import com.jparkbro.splash.api.SplashNavKey
import com.jparkbro.splash.impl.SplashRoot

/** [SplashNavKey.Splash]의 contentKey */
const val SPLASH_CONTENT_KEY = "SplashNavKey.Splash"

@OptIn(ExperimentalSharedTransitionApi::class)
fun EntryProviderScope<NavKey>.splashEntry(
    navigator: Navigator,
    sharedTransitionScope: SharedTransitionScope,
    // 딥링크 등 app 모듈이 홈 진입 시점에 추가로 할 일이 있을 때 오버라이드한다.
    onNavigateToHome: () -> Unit = navigator::navigateToHomeMain,
) {
    entry<SplashNavKey.Splash>(clazzContentKey = { SPLASH_CONTENT_KEY }) {
        SplashRoot(
            onNavigateToHome = onNavigateToHome,
            onNavigateToLogin = navigator::navigateToLogin,
            sharedTransitionScope = sharedTransitionScope,
        )
    }
}
