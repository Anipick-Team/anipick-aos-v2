package com.jparkbro.auth.impl.email.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.email.login.EmailLoginAction
import com.jparkbro.auth.impl.email.login.EmailLoginState
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun EmailLoginFields(
    state: EmailLoginState,
    onAction: (EmailLoginAction) -> Unit,
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
