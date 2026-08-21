package com.jparkbro.auth.impl.password.reset

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
import com.jparkbro.auth.impl.password.reset.components.PasswordResetFields
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.ui.effect.ObserveAsEvents
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

    AuthFormLayout(
        title = "비밀번호 찾기",
        subtitle = "회원 서비스 이용을 위해 비밀번호를 찾아주세요.",
        onBackClick = { onAction(PasswordResetAction.OnBackClick) },
        bottom = {
            AniPickButton(
                text = "비밀번호 변경 완료",
                onClick = { onAction(PasswordResetAction.OnResetClick) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isResetEnabled,
                isLoading = state.isLoading,
            )
        },
    ) {
        PasswordResetFields(
            state = state,
            onAction = onAction,
            checkPasswordFocusRequester = checkPasswordFocusRequester,
            focusManager = focusManager,
        )
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
