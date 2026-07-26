package com.jparkbro.auth.impl.login

import android.app.Activity

sealed interface LoginAction {
    data class OnKakaoLoginClick(val activity: Activity) : LoginAction
    data class OnGoogleLoginClick(val activity: Activity) : LoginAction
    data object OnEmailLoginClick : LoginAction
    data object OnEmailSignupClick : LoginAction
    data object OnProblemClick : LoginAction
}
