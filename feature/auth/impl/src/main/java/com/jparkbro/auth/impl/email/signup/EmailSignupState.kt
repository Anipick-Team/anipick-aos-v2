package com.jparkbro.auth.impl.email.signup

import androidx.compose.foundation.text.input.TextFieldState

data class EmailSignupState(
    val emailState: TextFieldState = TextFieldState(),
    val passwordState: TextFieldState = TextFieldState(),
    val emailError: String? = null,
    val isPasswordValid: Boolean = false,
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val isSignupEnabled: Boolean = false,
    val isAgeChecked: Boolean = false,
    val isTermsOfServiceChecked: Boolean = false,
    val isPrivacyPolicyChecked: Boolean = false,
    val termsError: String? = null,
) {
    /** "모두 동의" 체크박스 상태 — 나머지 세 항목이 전부 체크됐을 때만 true. */
    val isAgreeAll: Boolean
        get() = isAgeChecked && isTermsOfServiceChecked && isPrivacyPolicyChecked
}
