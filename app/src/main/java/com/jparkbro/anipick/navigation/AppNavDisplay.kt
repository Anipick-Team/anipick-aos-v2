package com.jparkbro.anipick.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.jparkbro.auth.impl.navigation.authEntry
import com.jparkbro.explore.impl.navigation.exploreEntry
import com.jparkbro.home.impl.navigation.homeEntry
import com.jparkbro.mypage.impl.navigation.myPageEntry
import com.jparkbro.ranking.impl.navigation.rankingEntry
import com.jparkbro.splash.impl.navigation.splashEntry
import kr.agromarket.at.core.navigation.NavigationState
import kr.agromarket.at.core.navigation.Navigator
import kr.agromarket.at.core.navigation.toEntries

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
        )
    }
}