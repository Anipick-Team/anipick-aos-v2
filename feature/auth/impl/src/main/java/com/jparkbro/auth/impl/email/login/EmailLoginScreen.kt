package com.jparkbro.auth.impl.email.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.auth.impl.component.AuthScaffold
import com.jparkbro.auth.impl.component.AuthScreenHeader
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
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

    AuthScaffold { innerPadding ->
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
                    title = "이메일 로그인",
                    subtitle = "회원 서비스 이용을 위해 로그인 해주세요.",
                    onBackClick = { onAction(EmailLoginAction.OnBackClick) },
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
                                placeholder = "이메일 입력",
                                maxLength = 50,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next,
                                ),
                                onKeyboardAction = { passwordFocusRequester.requestFocus() },
                            )
                        },
                    )
                    state.emailError?.let { errorMessage ->
                        AniPickErrorText(errorMessage)
                    }
                }
                AniPickLabeledField(
                    label = "비밀번호",
                    textField = {
                        AniPickBaseTextField(
                            state = state.passwordState,
                            modifier = Modifier.focusRequester(passwordFocusRequester),
                            type = TextFieldType.PASSWORD,
                            placeholder = "비밀번호 입력",
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            onKeyboardAction = {
                                focusManager.clearFocus()
                                if (state.isLoginEnabled) {
                                    onAction(EmailLoginAction.OnLoginClick)
                                }
                            },
                            actions = {
                                AniPickPasswordVisibilityToggleIcon(
                                    showPassword = state.showPassword,
                                    onToggle = { onAction(EmailLoginAction.OnPasswordVisibilityToggle) },
                                )
                            },
                            showPassword = state.showPassword,
                            maxLength = 50,
                        )
                    },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "이메일 회원가입",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.textGray,
                        modifier = Modifier
                            .clickable(
                                onClick = { onAction(EmailLoginAction.OnEmailSignupClick) }
                            )
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(20.dp),
                        thickness = 1.dp,
                        color = AniPickTheme.colors.textGray
                    )
                    Text(
                        text = "비밀번호 찾기",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.textGray,
                        modifier = Modifier
                            .clickable(
                                onClick = { onAction(EmailLoginAction.OnFindPasswordClick) }
                            )
                    )
                }
                AniPickErrorText(
                    message = state.loginError,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            AniPickButton(
                text = "로그인",
                onClick = { onAction(EmailLoginAction.OnLoginClick) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isLoginEnabled,
                isLoading = state.isLoading,
            )
        }
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
