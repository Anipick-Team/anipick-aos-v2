package com.jparkbro.auth.impl.password.verification.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.password.verification.PasswordVerificationAction
import com.jparkbro.auth.impl.password.verification.PasswordVerificationState
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun PasswordVerificationFields(
    state: PasswordVerificationState,
    onAction: (PasswordVerificationAction) -> Unit,
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
                    placeholder = "이메일을 입력해주세요",
                    maxLength = 50,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { focusManager.clearFocus() },
                )
            },
            fieldTrailingContent = {
                VerificationCodeRequestButton(
                    requestState = state.codeRequestState,
                    isEmailValid = state.isEmailValid,
                    onClick = { onAction(PasswordVerificationAction.OnRequestVerificationCodeClick) },
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
                        state.codeExpiresInSeconds?.takeIf { it > 0 }?.let { remainingSeconds ->
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
        state.codeError?.let { errorMessage ->
            AniPickErrorText(errorMessage)
        }
    }
}
