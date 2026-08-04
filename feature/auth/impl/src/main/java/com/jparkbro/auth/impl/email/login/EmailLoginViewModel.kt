package com.jparkbro.auth.impl.email.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.core.ui.validation.EmailPatternValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class EmailLoginViewModel(
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(EmailLoginState())
    val state: StateFlow<EmailLoginState> = _state.asStateFlow()

    private val _events = Channel<EmailLoginEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeEmailValidation()
        observeLoginButtonEnabled()
    }

    fun onAction(action: EmailLoginAction) {
        when (action) {
            EmailLoginAction.OnPasswordVisibilityToggle -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
            EmailLoginAction.OnLoginClick -> login()
            EmailLoginAction.OnDialogDismiss -> {
                _state.update { it.copy(showAccountDeletedDialog = false) }
            }
            EmailLoginAction.OnEmailSignupClick,
            EmailLoginAction.OnFindPasswordClick,
            EmailLoginAction.OnBackClick -> Unit
        }
    }

    private fun observeEmailValidation() {
        viewModelScope.launch {
            snapshotFlow { _state.value.emailState.text.toString() }
                .distinctUntilChanged()
                .collect { email ->
                    val emailError = if (email.isNotEmpty() && !EmailPatternValidator.matches(email)) {
                        "올바른 이메일 형식이 아닙니다."
                    } else {
                        null
                    }
                    _state.update { it.copy(emailError = emailError) }
                }
        }
    }

    private fun observeLoginButtonEnabled() {
        viewModelScope.launch {
            combine(
                snapshotFlow { _state.value.emailState.text.toString() },
                snapshotFlow { _state.value.passwordState.text.toString() },
                _state.map { it.isLoading }.distinctUntilChanged(),
            ) { email, password, isLoading ->
                email.isNotEmpty() && EmailPatternValidator.matches(email) && password.isNotEmpty() && !isLoading
            }
                .distinctUntilChanged()
                .collect { isLoginEnabled ->
                    _state.update { it.copy(isLoginEnabled = isLoginEnabled) }
                }
        }
    }

    private fun login() {
        val email = _state.value.emailState.text.toString()
        val password = _state.value.passwordState.text.toString()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loginError = null) }

            authRepository.loginWithEmail(email, password)
                .onSuccess { reviewCompletedYn ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(
                        if (reviewCompletedYn) EmailLoginEvent.NavigateToHome else EmailLoginEvent.NavigateToPreferenceSetup
                    )
                }
                .onFailure { error ->
                    Timber.e("이메일 로그인 실패: $error")
                    _state.update { it.copy(isLoading = false) }
                    handleLoginFailure(error)
                }
        }
    }

    private fun handleLoginFailure(error: DataError.Network) {
        when (error) {
            is DataError.Network.Api -> {
                if (error.code == 132) {
                    _state.update { it.copy(showAccountDeletedDialog = true) }
                } else {
                    _state.update { it.copy(loginError = error.message) }
                }
            }
            DataError.Network.NO_INTERNET -> {
                globalSnackbarManager.showSnackbar("네트워크 연결을 확인해주세요.")
            }
            else -> {
                _state.update { it.copy(loginError = "알 수 없는 오류가 발생했습니다.") }
            }
        }
    }

}
