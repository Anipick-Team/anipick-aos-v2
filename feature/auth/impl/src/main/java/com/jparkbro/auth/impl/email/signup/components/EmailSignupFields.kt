package com.jparkbro.auth.impl.email.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.email.signup.EmailSignupAction
import com.jparkbro.auth.impl.email.signup.EmailSignupState
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.model.TextFieldType

@Composable
internal fun EmailSignupFields(
    state: EmailSignupState,
    onAction: (EmailSignupAction) -> Unit,
    passwordFocusRequester: FocusRequester,
    focusManager: FocusManager,
) {
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
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AniPickLabeledField(
            label = "비밀번호",
            labelTrailingContent = {
                AniPickValidationCheckIcon(isValid = state.isPasswordValid)
            },
            textField = {
                AniPickBaseTextField(
                    state = state.passwordState,
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    type = TextFieldType.PASSWORD,
                    placeholder = "8~16자의 영문, 숫자, 특수문자",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = {
                        focusManager.clearFocus()
                        if (state.isSignupEnabled) {
                            onAction(EmailSignupAction.OnSignUpClick)
                        }
                    },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
                            showPassword = state.showPassword,
                            onToggle = { onAction(EmailSignupAction.OnPasswordVisibilityToggle) },
                        )
                    },
                    showPassword = state.showPassword,
                    maxLength = 20,
                )
            },
        )
        state.passwordError?.let { errorMessage ->
            AniPickErrorText(errorMessage)
        }
    }
}
