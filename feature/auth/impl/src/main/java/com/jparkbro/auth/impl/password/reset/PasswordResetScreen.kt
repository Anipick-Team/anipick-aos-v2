package com.jparkbro.auth.impl.password.reset

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthScreenHeader
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.icon.Check
import com.jparkbro.core.designsystem.icon.VisibilityOff
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PasswordResetRoot(
    email: String,
    onNavigateToEmailLogin: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: PasswordResetViewModel = koinViewModel(parameters = { parametersOf(email) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PasswordResetEvent.NavigateToEmailLogin -> onNavigateToEmailLogin()
        }
    }

    PasswordResetScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PasswordResetAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun PasswordResetScreen(
    state: PasswordResetState,
    onAction: (PasswordResetAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val checkPasswordFocusRequester = remember { FocusRequester() }

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
                    onBackClick = { onAction(PasswordResetAction.OnBackClick) },
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "새 비밀번호",
                        labelTrailingContent = {
                            Icon(
                                imageVector = Check,
                                contentDescription = "체크",
                                tint = if (state.isNewPasswordValid) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        textField = {
                            AniPickBaseTextField(
                                state = state.newPasswordState,
                                type = TextFieldType.PASSWORD,
                                placeholder = "8~16자의 영문, 숫자, 특수문자",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                onKeyboardAction = { checkPasswordFocusRequester.requestFocus() },
                                actions = {
                                    Icon(
                                        imageVector = if (state.showPassword) VisibilityOn else VisibilityOff,
                                        contentDescription = "비밀번호 표시 전환",
                                        tint = AniPickTheme.colors.textGray,
                                        modifier = Modifier
                                            .clickable(
                                                onClick = { onAction(PasswordResetAction.OnPasswordVisibilityToggle) }
                                            )
                                    )
                                },
                                showPassword = state.showPassword,
                                maxLength = 20,
                            )
                        },
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "새 비밀번호 확인",
                        labelTrailingContent = {
                            Icon(
                                imageVector = Check,
                                contentDescription = "체크",
                                tint = if (state.isPasswordMatch) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        textField = {
                            AniPickBaseTextField(
                                state = state.checkNewPasswordState,
                                modifier = Modifier.focusRequester(checkPasswordFocusRequester),
                                type = TextFieldType.PASSWORD,
                                placeholder = "새 비밀번호를 다시 한 번 입력해주세요.",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                onKeyboardAction = { focusManager.clearFocus() },
                                actions = {
                                    Icon(
                                        imageVector = if (state.showPassword) VisibilityOn else VisibilityOff,
                                        contentDescription = "비밀번호 표시 전환",
                                        tint = AniPickTheme.colors.textGray,
                                        modifier = Modifier
                                            .clickable(
                                                onClick = { onAction(PasswordResetAction.OnPasswordVisibilityToggle) }
                                            )
                                    )
                                },
                                showPassword = state.showPassword,
                                maxLength = 20,
                            )
                        },
                    )
                    state.error?.let { message ->
                        Text(
                            text = message,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
            }
            AniPickButton(
                text = "비밀번호 변경 완료",
                onClick = { onAction(PasswordResetAction.OnResetClick) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isResetEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PasswordResetScreenPreview() {
    PasswordResetScreen(
        state = PasswordResetState(),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun PasswordResetScreenValidPreview() {
    PasswordResetScreen(
        state = PasswordResetState(
            isNewPasswordValid = true,
            isPasswordMatch = true,
            isResetEnabled = true,
        ),
        onAction = {}
    )
}
