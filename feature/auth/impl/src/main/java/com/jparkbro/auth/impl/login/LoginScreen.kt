package com.jparkbro.auth.impl.login

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.jparkbro.auth.impl.login.components.LoginActions
import com.jparkbro.core.designsystem.component.AniPickBrandHeader
import com.jparkbro.core.designsystem.component.AniPickBrandHeaderKey
import com.jparkbro.core.designsystem.extension.context.requireActivity
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun LoginRoot(
    sharedTransitionScope: SharedTransitionScope,
    onNavigateToHome: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val activity = context.requireActivity()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.NavigateToHome -> onNavigateToHome()
            LoginEvent.NavigateToPreferenceSetup -> onNavigateToPreferenceSetup()
        }
    }

    val headerModifier = with(sharedTransitionScope) {
        Modifier.sharedElement(
            sharedTransitionScope.rememberSharedContentState(key = AniPickBrandHeaderKey),
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        )
    }

    LoginScreen(
        activity = activity,
        modifier = headerModifier,
        onAction = { action ->
            when (action) {
                LoginAction.OnEmailLoginClick -> onNavigateToEmailLogin()
                LoginAction.OnEmailSignupClick -> onNavigateToEmailSignup()
                LoginAction.OnProblemClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                    context.startActivity(intent)
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun LoginScreen(
    activity: Activity,
    modifier: Modifier = Modifier,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AniPickTheme.colors.white),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(100.dp, Alignment.CenterVertically)
    ) {
        AniPickBrandHeader(modifier = modifier)

        LoginActions(
            activity = activity,
            onAction = onAction
        )

        Text(
            text = "로그인에 문제가 있으신가요?",
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
            modifier = Modifier
                .background(AniPickTheme.colors.white)
                .border(1.dp, AniPickTheme.colors.textGray, RoundedCornerShape((21.5).dp))
                .clip(RoundedCornerShape((21.5).dp))
                .clickable(
                    onClick = { onAction(LoginAction.OnProblemClick) }
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        activity = LocalContext.current.requireActivity(),
        onAction = {}
    )
}
