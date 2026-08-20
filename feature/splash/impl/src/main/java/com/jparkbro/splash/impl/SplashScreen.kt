package com.jparkbro.splash.impl

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.jparkbro.core.designsystem.component.AniPickBrandHeader
import com.jparkbro.core.designsystem.component.AniPickBrandHeaderKey
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun SplashRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: SplashViewModel = koinViewModel(),
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SplashEvent.NavigateToHome -> onNavigateToHome()
            SplashEvent.NavigateToLogin -> onNavigateToLogin()
        }
    }

    val headerModifier = with(sharedTransitionScope) {
        Modifier.sharedElement(
            sharedTransitionScope.rememberSharedContentState(key = AniPickBrandHeaderKey),
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        )
    }

    SplashScreen(modifier = headerModifier)
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AniPickTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        AniPickBrandHeader(modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
