package com.jparkbro.anipick

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jparkbro.anipick.navigation.AniPickBottomNavigation
import com.jparkbro.anipick.navigation.AppNavDisplay
import com.jparkbro.anipick.navigation.TOP_LEVEL_ITEMS
import com.jparkbro.core.designsystem.theme.AniPick_v2Theme
import com.jparkbro.splash.api.SplashNavKey
import kr.agromarket.at.core.navigation.Navigator
import kr.agromarket.at.core.navigation.rememberNavigationState

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

                Scaffold(
                    contentWindowInsets = WindowInsets(0.dp),
                    snackbarHost = { }
                ) { innerPadding ->
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
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}
