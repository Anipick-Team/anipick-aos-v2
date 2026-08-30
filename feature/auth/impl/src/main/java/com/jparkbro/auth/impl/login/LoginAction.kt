package com.jparkbro.auth.impl.login

import android.app.Activity

sealed interface LoginAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : LoginAction

    data class OnKakaoLoginClick(val activity: Activity) : LoginAction
    data class OnGoogleLoginClick(val activity: Activity) : LoginAction
    data object OnEmailLoginClick : Navigation
    data object OnEmailSignupClick : Navigation
    data object OnProblemClick : Navigation
}
