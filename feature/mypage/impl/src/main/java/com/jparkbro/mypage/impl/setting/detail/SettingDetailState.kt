package com.jparkbro.mypage.impl.setting.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.mypage.api.SettingDetailType

data class SettingDetailState(
    val type: SettingDetailType = SettingDetailType.Nickname,
    val currentNickname: String? = null,
    val currentEmail: String? = null,
    val nicknameState: TextFieldState = TextFieldState(),
    val nicknameError: String? = null,
    val emailState: TextFieldState = TextFieldState(),
    val isNewEmailValid: Boolean = false,
    val emailPasswordState: TextFieldState = TextFieldState(),
    val emailError: String? = null,
    val emailPasswordError: String? = null,
    val currentPasswordState: TextFieldState = TextFieldState(),
    val currentPasswordError: String? = null,
    val newPasswordState: TextFieldState = TextFieldState(),
    val newPasswordError: String? = null,
    val checkNewPasswordState: TextFieldState = TextFieldState(),
    val newPasswordConfirmError: String? = null,
    val isNewPasswordValid: Boolean = false,
    val isPasswordMatch: Boolean = false,
    val showPassword: Boolean = false,
    val showWithdrawDialog: Boolean = false,
    val isLoading: Boolean = false,
) {
    val isChangeNicknameEnabled: Boolean
        get() = !isLoading &&
                nicknameState.text.isNotBlank()

    val isChangeEmailEnabled: Boolean
        get() = !isLoading &&
                emailPasswordState.text.isNotBlank() &&
                emailState.text.isNotBlank() &&
                isNewEmailValid

    val isChangePasswordEnabled: Boolean
        get() = !isLoading &&
                currentPasswordState.text.isNotBlank() &&
                isPasswordMatch &&
                isNewPasswordValid
}
