package com.jparkbro.auth.impl.password.reset

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.core.ui.validation.PasswordPatternValidator
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

class PasswordResetViewModel(
    private val email: String,
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordResetState())
    val state: StateFlow<PasswordResetState> = _state.asStateFlow()

    private val _events = Channel<PasswordResetEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeNewPasswordValidation()
        observePasswordMatch()
        observeResetButtonEnabled()
    }

    fun onAction(action: PasswordResetAction) {
        when (action) {
            PasswordResetAction.OnPasswordVisibilityToggle -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
            PasswordResetAction.OnResetClick -> resetPassword()
            else -> Unit
        }
    }

    private fun observeNewPasswordValidation() {
        viewModelScope.launch {
            snapshotFlow { _state.value.newPasswordState.text.toString() }
                .distinctUntilChanged()
                .collect { newPassword ->
                    val isValid = PasswordPatternValidator.isValidPassword(newPassword).isValidPassword
                    _state.update { it.copy(isNewPasswordValid = isValid) }
                }
        }
    }

    private fun observePasswordMatch() {
        viewModelScope.launch {
            combine(
                snapshotFlow { _state.value.newPasswordState.text.toString() },
                snapshotFlow { _state.value.checkNewPasswordState.text.toString() },
            ) { newPassword, checkNewPassword ->
                checkNewPassword.isNotEmpty() && newPassword == checkNewPassword
            }
                .distinctUntilChanged()
                .collect { isMatch ->
                    _state.update { it.copy(isPasswordMatch = isMatch) }
                }
        }
    }

    private fun observeResetButtonEnabled() {
        viewModelScope.launch {
            combine(
                snapshotFlow { _state.value.newPasswordState.text.toString() },
                snapshotFlow { _state.value.checkNewPasswordState.text.toString() },
                _state.map { it.isNewPasswordValid }.distinctUntilChanged(),
                _state.map { it.isPasswordMatch }.distinctUntilChanged(),
                _state.map { it.isLoading }.distinctUntilChanged(),
            ) { newPassword, checkNewPassword, isNewPasswordValid, isPasswordMatch, isLoading ->
                newPassword.isNotEmpty() && checkNewPassword.isNotEmpty() &&
                    isNewPasswordValid && isPasswordMatch && !isLoading
            }
                .distinctUntilChanged()
                .collect { isResetEnabled ->
                    _state.update { it.copy(isResetEnabled = isResetEnabled) }
                }
        }
    }

    private fun resetPassword() {
        val newPassword = _state.value.newPasswordState.text.toString()
        val checkNewPassword = _state.value.checkNewPasswordState.text.toString()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            authRepository.resetPassword(email, newPassword, checkNewPassword)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(PasswordResetEvent.NavigateToEmailLogin)
                }
                .onFailure { error ->
                    Timber.e("비밀번호 재설정 실패: $error")
                    _state.update { it.copy(isLoading = false) }
                    handleResetFailure(error)
                }
        }
    }

    private fun handleResetFailure(error: DataError.Network) {
        when (error) {
            is DataError.Network.Api -> {
                _state.update { it.copy(error = error.message) }
            }
            DataError.Network.NO_INTERNET -> {
                globalSnackbarManager.showSnackbar("네트워크 연결을 확인해주세요.")
            }
            else -> {
                globalSnackbarManager.showSnackbar("알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}
