package com.jparkbro.auth.impl.email.signup

sealed interface EmailSignupAction {
    data object OnPasswordVisibilityToggle : EmailSignupAction
    data object OnSignUpClick : EmailSignupAction
    data object OnBackClick : EmailSignupAction

    // 약관 동의
    data object OnAgreeAllToggle : EmailSignupAction
    data object OnAgeToggle : EmailSignupAction
    data object OnTermsOfServiceToggle : EmailSignupAction
    data object OnTermsOfServiceDetailClick : EmailSignupAction
    data object OnPrivacyPolicyToggle : EmailSignupAction
    data object OnPrivacyPolicyDetailClick : EmailSignupAction
}
