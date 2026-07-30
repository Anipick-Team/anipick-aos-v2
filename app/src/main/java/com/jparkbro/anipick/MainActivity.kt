package com.jparkbro.anipick

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.anipick.navigation.AniPickBottomNavigation
import com.jparkbro.anipick.navigation.AppNavDisplay
import com.jparkbro.anipick.navigation.TOP_LEVEL_ITEMS
import com.jparkbro.auth.api.navigateToLogin
import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.designsystem.component.AniPickSnackbar
import com.jparkbro.core.designsystem.theme.AniPick_v2Theme
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.splash.api.SplashNavKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kr.agromarket.at.core.navigation.Navigator
import kr.agromarket.at.core.navigation.rememberNavigationState
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /** 종료 확인 콜백 */
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish()
                } else {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this@MainActivity, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        setContent {
            AniPick_v2Theme {
                val navigationState = rememberNavigationState(
                    startKey = SplashNavKey.Splash,
                    topLevelKeys = TOP_LEVEL_ITEMS.keys,
                )
                val navigator = remember(navigationState) { Navigator(navigationState) }

                val tokenProvider = koinInject<TokenProvider>()
                LaunchedEffect(tokenProvider) {
                    tokenProvider.isLoggedIn
                        .distinctUntilChanged()
                        .drop(1) // 콜드 스타트 시점의 최초 상태는 무시 (Splash가 자체적으로 처리)
                        .filter { isLoggedIn -> !isLoggedIn }
                        .collect {
                            // refreshToken까지 만료되어 세션이 끊긴 경우: 로그인 화면으로 강제 이동
                            navigator.navigateToLogin()
                        }
                }

                val globalSnackbarManager = koinInject<GlobalSnackbarManager>()
                val snackbarMessages by globalSnackbarManager.messages.collectAsStateWithLifecycle()

                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavDisplay(
                        bottomNavigation = {
                            AniPickBottomNavigation(
                                currentKey = navigationState.currentTopLevelKey,
                                onNavigate = { navigator.navigate(it) },
                            )
                        },
                        navigationState = navigationState,
                        navigator = navigator,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    snackbarMessages.firstOrNull()?.let { message ->
                        AniPickSnackbar(
                            message = message,
                            onDismiss = { globalSnackbarManager.dismissCurrent() },
                        )
                    }
                }
            }
        }
    }
}
