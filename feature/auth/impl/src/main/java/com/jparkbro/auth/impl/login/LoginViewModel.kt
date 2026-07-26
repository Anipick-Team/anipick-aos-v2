package com.jparkbro.auth.impl.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(

) : ViewModel() {

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            // TODO: 카카오 로그인 SDK 연동, 성공 시 sendEvent(LoginEvent.NavigateToPreferenceSetup)
            is LoginAction.OnKakaoLoginClick -> {}
            // TODO: 구글 로그인 SDK 연동, 성공 시 sendEvent(LoginEvent.NavigateToPreferenceSetup)
            is LoginAction.OnGoogleLoginClick -> {}
            else -> Unit
        }
    }

    private fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
