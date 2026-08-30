package com.jparkbro.auth.impl.email.signup

sealed interface EmailSignupAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : EmailSignupAction

    data object OnPasswordVisibilityToggle : EmailSignupAction
    data object OnSignUpClick : EmailSignupAction
    data object OnBackClick : Navigation

    // 약관 동의
    data object OnAgreeAllToggle : EmailSignupAction
    data object OnAgeToggle : EmailSignupAction
    data object OnTermsOfServiceToggle : EmailSignupAction
    data object OnTermsOfServiceDetailClick : Navigation
    data object OnPrivacyPolicyToggle : EmailSignupAction
    data object OnPrivacyPolicyDetailClick : Navigation
}
