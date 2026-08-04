package com.jparkbro.auth.impl.password.reset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthErrorText
import com.jparkbro.auth.impl.component.AuthScaffold
import com.jparkbro.auth.impl.component.AuthScreenHeader
import com.jparkbro.auth.impl.component.PasswordVisibilityToggleIcon
import com.jparkbro.auth.impl.component.ValidationCheckIcon
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.model.TextFieldType
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
                            ValidationCheckIcon(isValid = state.isNewPasswordValid)
                        },
                        textField = {
                            AniPickBaseTextField(
                                state = state.newPasswordState,
                                type = TextFieldType.PASSWORD,
                                placeholder = "8~16자의 영문, 숫자, 특수문자",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                onKeyboardAction = { checkPasswordFocusRequester.requestFocus() },
                                actions = {
                                    PasswordVisibilityToggleIcon(
                                        showPassword = state.showPassword,
                                        onToggle = { onAction(PasswordResetAction.OnPasswordVisibilityToggle) },
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
                            ValidationCheckIcon(isValid = state.isPasswordMatch)
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
                                    PasswordVisibilityToggleIcon(
                                        showPassword = state.showPassword,
                                        onToggle = { onAction(PasswordResetAction.OnPasswordVisibilityToggle) },
                                    )
                                },
                                showPassword = state.showPassword,
                                maxLength = 20,
                            )
                        },
                    )
                    AuthErrorText(state.error)
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
