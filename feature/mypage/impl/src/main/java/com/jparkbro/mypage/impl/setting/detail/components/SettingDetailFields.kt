package com.jparkbro.mypage.impl.setting.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.mypage.impl.setting.detail.SettingDetailAction
import com.jparkbro.mypage.impl.setting.detail.SettingDetailState

@Composable
internal fun NicknameFields(state: SettingDetailState) {
    val focusManager = LocalFocusManager.current

    CurrentValueField(label = "기존 닉네임", value = state.currentNickname.orEmpty())
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickLabeledField(
            label = "새 닉네임",
            textField = {
                AniPickBaseTextField(
                    state = state.nicknameState,
                    type = TextFieldType.TEXT,
                    placeholder = "새 닉네임 입력",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { focusManager.clearFocus() },
                )
            },
        )
        state.nicknameError?.let { errorMessage -> AniPickErrorText(errorMessage) }
    }
}

@Composable
internal fun EmailFields(
    state: SettingDetailState,
    onAction: (SettingDetailAction) -> Unit,
    emailPasswordFocusRequester: FocusRequester,
) {
    val focusManager = LocalFocusManager.current

    CurrentValueField(label = "기존 이메일", value = state.currentEmail.orEmpty())
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickLabeledField(
            label = "새 이메일",
            textField = {
                AniPickBaseTextField(
                    state = state.emailState,
                    type = TextFieldType.TEXT,
                    placeholder = "새 이메일 입력",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    onKeyboardAction = { emailPasswordFocusRequester.requestFocus() },
                )
            },
        )
        state.emailError?.let { errorMessage -> AniPickErrorText(errorMessage) }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickLabeledField(
            label = "비밀번호",
            textField = {
                AniPickBaseTextField(
                    state = state.emailPasswordState,
                    modifier = Modifier.focusRequester(emailPasswordFocusRequester),
                    type = TextFieldType.PASSWORD,
                    placeholder = "본인 확인을 위해 비밀번호를 입력하세요",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { focusManager.clearFocus() },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
                            showPassword = state.showPassword,
                            onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                        )
                    },
                    showPassword = state.showPassword,
                )
            },
        )
        state.emailPasswordError?.let { errorMessage -> AniPickErrorText(errorMessage) }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "• 반드시 본인 명의의 이메일을 입력해주세요.",
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
            Text(
                text = "• 본 이메일은 계정 분실 시 아이디 및 비밀번호 찾기, 개인정보 관련 주요 고지사항 안내 등에 사용됩니다.",
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
        }
    }
}

@Composable
internal fun PasswordFields(
    state: SettingDetailState,
    onAction: (SettingDetailAction) -> Unit,
    newPasswordFocusRequester: FocusRequester,
    checkNewPasswordFocusRequester: FocusRequester,
) {
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickLabeledField(
            label = "현재 비밀번호",
            textField = {
                AniPickBaseTextField(
                    state = state.currentPasswordState,
                    type = TextFieldType.PASSWORD,
                    placeholder = "현재 비밀번호를 입력하세요",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onKeyboardAction = { newPasswordFocusRequester.requestFocus() },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
                            showPassword = state.showPassword,
                            onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                        )
                    },
                    showPassword = state.showPassword,
                )
            },
        )
        state.currentPasswordError?.let { errorMessage -> AniPickErrorText(errorMessage) }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickLabeledField(
            label = "새 비밀번호",
            labelTrailingContent = { AniPickValidationCheckIcon(isValid = state.isNewPasswordValid) },
            textField = {
                AniPickBaseTextField(
                    state = state.newPasswordState,
                    modifier = Modifier.focusRequester(newPasswordFocusRequester),
                    type = TextFieldType.PASSWORD,
                    placeholder = "8~16자의 영문, 숫자, 특수문자",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onKeyboardAction = { checkNewPasswordFocusRequester.requestFocus() },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
                            showPassword = state.showPassword,
                            onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                        )
                    },
                    showPassword = state.showPassword,
                )
            },
        )
        state.newPasswordError?.let { errorMessage -> AniPickErrorText(errorMessage) }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickLabeledField(
            label = "새 비밀번호 확인",
            labelTrailingContent = { AniPickValidationCheckIcon(isValid = state.isPasswordMatch) },
            textField = {
                AniPickBaseTextField(
                    state = state.checkNewPasswordState,
                    modifier = Modifier.focusRequester(checkNewPasswordFocusRequester),
                    type = TextFieldType.PASSWORD,
                    placeholder = "새 비밀번호를 다시 입력하세요",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { focusManager.clearFocus() },
                    actions = {
                        AniPickPasswordVisibilityToggleIcon(
                            showPassword = state.showPassword,
                            onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                        )
                    },
                    showPassword = state.showPassword,
                )
            },
        )
        state.newPasswordConfirmError?.let { errorMessage -> AniPickErrorText(errorMessage) }
    }
}

@Composable
internal fun WithdrawalFields(state: SettingDetailState) {
    CurrentValueField(label = "계정 이메일", value = state.currentEmail.orEmpty())
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "탈퇴 시 주의사항",
            style = AniPickTheme.typography.h2,
            color = AniPickTheme.colors.black,
        )
        Text(
            text = "탈퇴 시, 회원정보는 당사의 개인정보처리방침에 따라 삭제 또는 격리하여 보존 조치되며, 삭제된 데이터는 복구가 불가능합니다. 서비스 내에서 남긴 리뷰는 탈퇴 후에 자동 삭제되지 않습니다.",
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.textGray,
        )
        AniPickErrorText("'회원탈퇴'를 누르는 것은 상기 안내사항을 모두 확인하였으며 이에 동의함을 의미합니다.")
    }
}
