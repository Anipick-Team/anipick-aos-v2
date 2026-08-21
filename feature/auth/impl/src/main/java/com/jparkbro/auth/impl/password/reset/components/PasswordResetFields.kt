package com.jparkbro.auth.impl.password.reset.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.password.reset.PasswordResetAction
import com.jparkbro.auth.impl.password.reset.PasswordResetState
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.model.TextFieldType

@Composable
internal fun PasswordResetFields(
    state: PasswordResetState,
    onAction: (PasswordResetAction) -> Unit,
    checkPasswordFocusRequester: FocusRequester,
    focusManager: FocusManager,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AniPickLabeledField(
            label = "새 비밀번호",
            labelTrailingContent = {
                AniPickValidationCheckIcon(isValid = state.isNewPasswordValid)
            },
            textField = {
                AniPickBaseTextField(
                    state = state.newPasswordState,
                    type = TextFieldType.PASSWORD,
                    placeholder = "8~16자의 영문, 숫자, 특수문자",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onKeyboardAction = { checkPasswordFocusRequester.requestFocus() },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
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
                AniPickValidationCheckIcon(isValid = state.isPasswordMatch)
            },
            textField = {
                AniPickBaseTextField(
                    state = state.checkNewPasswordState,
                    modifier = Modifier.focusRequester(checkPasswordFocusRequester),
                    type = TextFieldType.PASSWORD,
                    placeholder = "새 비밀번호를 다시 한 번 입력해주세요.",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = {
                        focusManager.clearFocus()
                        if (state.isResetEnabled) {
                            onAction(PasswordResetAction.OnResetClick)
                        }
                    },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
                            showPassword = state.showPassword,
                            onToggle = { onAction(PasswordResetAction.OnPasswordVisibilityToggle) },
                        )
                    },
                    showPassword = state.showPassword,
                    maxLength = 20,
                )
            },
        )
        state.error?.let { errorMessage ->
            AniPickErrorText(errorMessage)
        }
    }
}
