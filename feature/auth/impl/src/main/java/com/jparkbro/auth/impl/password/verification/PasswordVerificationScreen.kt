package com.jparkbro.auth.impl.password.verification

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthFormLayout
import com.jparkbro.auth.impl.password.verification.components.PasswordVerificationFields
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PasswordVerificationRoot(
    onNavigateToLogin: () -> Unit,
    onNavigateToPasswordReset: (email: String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PasswordVerificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PasswordVerificationEvent.NavigateToPasswordReset -> onNavigateToPasswordReset(event.email)
            PasswordVerificationEvent.NavigateToLogin -> onNavigateToLogin()
        }
    }

    PasswordVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PasswordVerificationAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun PasswordVerificationScreen(
    state: PasswordVerificationState,
    onAction: (PasswordVerificationAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    AuthFormLayout(
        title = "비밀번호 찾기",
        subtitle = "회원 서비스 이용을 위해 비밀번호를 찾아주세요.",
        onBackClick = { onAction(PasswordVerificationAction.OnBackClick) },
        bottom = {
            AniPickButton(
                text = "다음",
                onClick = { onAction(PasswordVerificationAction.OnVerifyClick) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isVerifyEnabled,
                isLoading = state.isLoading,
            )
        },
    ) {
        PasswordVerificationFields(
            state = state,
            onAction = onAction,
            focusManager = focusManager,
        )
    }

    if (state.showSnsLoginAlert) {
        AniPickDialog(
            title = "SNS로 간편 가입된 계정입니다.",
            message = "SNS로 로그인해주세요.",
            onDismissRequest = { onAction(PasswordVerificationAction.OnAlertDismiss) },
            confirmText = "SNS 로그인",
            onConfirm = { onAction(PasswordVerificationAction.OnSnsLoginClick) },
            dismissText = "닫기",
            onDismiss = { onAction(PasswordVerificationAction.OnAlertDismiss) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PasswordVerificationScreenPreview() {
    PasswordVerificationScreen(
        state = PasswordVerificationState(),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun PasswordVerificationScreenAlertPreview() {
    PasswordVerificationScreen(
        state = PasswordVerificationState(showSnsLoginAlert = true),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun PasswordVerificationScreenCooldownPreview() {
    PasswordVerificationScreen(
        state = PasswordVerificationState(
            codeRequestState = VerificationCodeRequestState.Cooldown(remainingSeconds = 25),
            codeExpiresInSeconds = 125,
        ),
        onAction = {}
    )
}
