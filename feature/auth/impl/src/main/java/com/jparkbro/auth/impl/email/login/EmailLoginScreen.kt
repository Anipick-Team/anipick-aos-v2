package com.jparkbro.auth.impl.email.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthFormLayout
import com.jparkbro.auth.impl.email.login.components.EmailLoginFields
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.ui.effect.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun EmailLoginRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    onNavigateToPasswordVerification: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: EmailLoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EmailLoginEvent.NavigateToHome -> onNavigateToHome()
            EmailLoginEvent.NavigateToPreferenceSetup -> onNavigateToPreferenceSetup()
        }
    }

    EmailLoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailLoginAction.OnBackClick -> onBackClick()
                EmailLoginAction.OnEmailSignupClick -> onNavigateToEmailSignup()
                EmailLoginAction.OnFindPasswordClick -> onNavigateToPasswordVerification()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EmailLoginScreen(
    state: EmailLoginState,
    onAction: (EmailLoginAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    AuthFormLayout(
        title = "이메일 로그인",
        subtitle = "회원 서비스 이용을 위해 로그인 해주세요.",
        onBackClick = { onAction(EmailLoginAction.OnBackClick) },
        bottom = {
            AniPickButton(
                text = "로그인",
                onClick = { onAction(EmailLoginAction.OnLoginClick) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isLoginEnabled,
                isLoading = state.isLoading,
            )
        },
    ) {
        EmailLoginFields(
            state = state,
            onAction = onAction,
            passwordFocusRequester = passwordFocusRequester,
            focusManager = focusManager,
        )
    }

    if (state.showAccountDeletedDialog) {
        AniPickDialog(
            title = "탈퇴된 계정입니다.",
            message = "자세한 사항은 고객센터로 문의해 주세요.\nteamanipick@gmail.com",
            onDismissRequest = { onAction(EmailLoginAction.OnDialogDismiss) },
            confirmText = "닫기",
            onConfirm = { onAction(EmailLoginAction.OnDialogDismiss) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EmailLoginScreenPreview() {
    EmailLoginScreen(
        state = EmailLoginState(),
        onAction = {}
    )
}
