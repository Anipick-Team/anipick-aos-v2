package com.jparkbro.auth.impl.login

import android.app.Activity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.jparkbro.core.designsystem.R
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
            .padding()
            .background(Color.White),
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
                .background(Color.White)
                .border(1.dp, AniPickTheme.colors.black, RoundedCornerShape((21.5).dp))
                .clip(RoundedCornerShape((21.5).dp))
                .clickable(
                    onClick = { onAction(LoginAction.OnProblemClick) }
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun LoginActions(
    activity: Activity,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        SocialLoginButton(
            imageVector = ImageVector.vectorResource(R.drawable.kakao_login),
            contentDescription = "kakao login",
            onClick = { onAction(LoginAction.OnKakaoLoginClick(activity)) }
        )
        SocialLoginButton(
            imageVector = ImageVector.vectorResource(R.drawable.google_login),
            contentDescription = "google login",
            onClick = { onAction(LoginAction.OnGoogleLoginClick(activity)) }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "이메일 로그인",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
                modifier = Modifier
                    .clickable(
                        onClick = { onAction(LoginAction.OnEmailLoginClick) }
                    )
            )
            VerticalDivider(
                modifier = Modifier
                    .height(20.dp),
                thickness = 1.dp,
                color = AniPickTheme.colors.textGray
            )
            Text(
                text = "이메일 회원가입",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
                modifier = Modifier
                    .clickable(
                        onClick = { onAction(LoginAction.OnEmailSignupClick) }
                    )
            )
        }
    }
}

@Composable
private fun SocialLoginButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Image(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        activity = LocalContext.current.requireActivity(),
        onAction = {}
    )
}
