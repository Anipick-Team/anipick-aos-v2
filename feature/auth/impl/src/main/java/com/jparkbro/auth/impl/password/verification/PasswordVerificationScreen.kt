package com.jparkbro.auth.impl.password.verification

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthScreenHeader
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PasswordVerificationRoot(
    onNavigateToPasswordReset: (email: String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PasswordVerificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PasswordVerificationEvent.NavigateToPasswordReset -> onNavigateToPasswordReset(event.email)
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp, 20.dp, 20.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                AuthScreenHeader(
                    title = "비밀번호 찾기",
                    subtitle = "회원 서비스 이용을 위해 비밀번호를 찾아주세요.",
                    onBackClick = { onAction(PasswordVerificationAction.OnBackClick) },
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "이메일",
                        textField = {
                            AniPickBaseTextField(
                                state = state.emailState,
                                type = TextFieldType.TEXT,
                                placeholder = "이메일을 입력해주세요",
                                maxLength = 50,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                onKeyboardAction = { focusManager.clearFocus() },
                            )
                        },
                        fieldTrailingContent = {
                            VerificationCodeRequestButton(
                                requestState = state.codeRequestState,
                                onClick = { onAction(PasswordVerificationAction.OnRequestVerificationCodeClick) },
                            )
                        },
                    )
                    state.emailError?.let { message ->
                        Text(
                            text = message,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "인증번호",
                        textField = {
                            AniPickBaseTextField(
                                state = state.codeState,
                                type = TextFieldType.TEXT,
                                placeholder = "인증번호를 입력해주세요",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                onKeyboardAction = {
                                    focusManager.clearFocus()
                                    if (state.isVerifyEnabled) {
                                        onAction(PasswordVerificationAction.OnVerifyClick)
                                    }
                                },
                                maxLength = 6,
                                actions = {
                                    state.codeExpiresInSeconds?.let { remainingSeconds ->
                                        val minutes = remainingSeconds / 60
                                        val seconds = remainingSeconds % 60
                                        Text(
                                            text = "%d:%02d".format(minutes, seconds),
                                            style = AniPickTheme.typography.body2,
                                            color = AniPickTheme.colors.point,
                                        )
                                    }
                                },
                            )
                        },
                    )
                    state.codeError?.let { message ->
                        Text(
                            text = message,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
            }
            AniPickButton(
                text = "다음",
                onClick = { onAction(PasswordVerificationAction.OnVerifyClick) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isVerifyEnabled,
                isLoading = state.isLoading,
            )
        }
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

/** "인증번호 받기" 자리에 들어가는 버튼. [requestState]에 따라 문구/클릭 가능 여부/카운트다운 표시가 바뀐다. */
@Composable
private fun VerificationCodeRequestButton(
    requestState: VerificationCodeRequestState,
    onClick: () -> Unit,
) {
    val label = when (requestState) {
        VerificationCodeRequestState.Idle -> "인증번호 받기"
        VerificationCodeRequestState.Loading -> "전송 중..."
        is VerificationCodeRequestState.Cooldown -> {
            val minutes = requestState.remainingSeconds / 60
            val seconds = requestState.remainingSeconds % 60
            "전송됨 %d:%02d".format(minutes, seconds)
        }
        VerificationCodeRequestState.Available -> "재발송하기"
    }
    val isEnabled = requestState is VerificationCodeRequestState.Idle ||
        requestState is VerificationCodeRequestState.Available

    AniPickButton(
        text = label,
        onClick = onClick,
        size = ButtonSize.S,
        enabled = isEnabled,
        isLoading = requestState is VerificationCodeRequestState.Loading,
    )
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
