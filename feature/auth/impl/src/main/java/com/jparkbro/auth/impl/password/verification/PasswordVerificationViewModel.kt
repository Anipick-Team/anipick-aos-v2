package com.jparkbro.auth.impl.password.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import timber.log.Timber

class PasswordVerificationViewModel(
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordVerificationState())
    val state: StateFlow<PasswordVerificationState> = _state.asStateFlow()

    private val _events = Channel<PasswordVerificationEvent>()
    val events = _events.receiveAsFlow()

    private var resendCooldownJob: Job? = null
    private var codeExpiryJob: Job? = null

    fun onAction(action: PasswordVerificationAction) {
        when (action) {
            PasswordVerificationAction.OnRequestVerificationCodeClick -> requestVerificationCode()
            PasswordVerificationAction.OnVerifyClick -> verifyCode()
            PasswordVerificationAction.OnAlertDismiss -> {
                _state.update { it.copy(showSnsLoginAlert = false) }
            }
            else -> Unit
        }
    }

    private fun requestVerificationCode() {
        val email = _state.value.emailState.text.toString()

        viewModelScope.launch {
            _state.update { it.copy(codeRequestState = VerificationCodeRequestState.Loading, emailError = null) }
            // 응답을 기다리지 않고 통신 시작과 동시에 재전송 쿨다운(30초)을 시작한다.
            startResendCooldown()

            authRepository.sendEmailVerification(email)
                .onSuccess {
                    startCodeExpiryCountdown()
                }
                .onFailure { error ->
                    Timber.e("인증번호 발송 실패: $error")
                    handleSendFailure(error)
                }
        }
    }

    private fun handleSendFailure(error: DataError.Network) {
        when (error) {
            is DataError.Network.Api -> {
                _state.update { it.copy(emailError = error.message) }
            }
            DataError.Network.NO_INTERNET -> {
                globalSnackbarManager.showSnackbar("네트워크 연결을 확인해주세요.")
            }
            else -> {
                globalSnackbarManager.showSnackbar("알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    private fun verifyCode() {
        val email = _state.value.emailState.text.toString()
        val code = _state.value.codeState.text.toString()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, codeError = null) }

            authRepository.verifyEmailCode(email, code)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(PasswordVerificationEvent.NavigateToPasswordReset(email))
                }
                .onFailure { error ->
                    Timber.e("인증번호 확인 실패: $error")
                    _state.update { it.copy(isLoading = false) }
                    handleVerifyFailure(error)
                }
        }
    }

    private fun handleVerifyFailure(error: DataError.Network) {
        when (error) {
            is DataError.Network.Api -> {
                _state.update { it.copy(codeError = error.message) }
            }
            DataError.Network.NO_INTERNET -> {
                globalSnackbarManager.showSnackbar("네트워크 연결을 확인해주세요.")
            }
            else -> {
                globalSnackbarManager.showSnackbar("알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    /** "재전송" 버튼을 30초간 막아뒀다가 풀어준다. API 응답과 무관하게 통신 시작 시점부터 돈다. */
    private fun startResendCooldown() {
        resendCooldownJob?.cancel()
        resendCooldownJob = viewModelScope.launch {
            var remainingSeconds = RESEND_COOLDOWN_SECONDS
            while (remainingSeconds > 0) {
                _state.update { it.copy(codeRequestState = VerificationCodeRequestState.Cooldown(remainingSeconds)) }
                delay(1_000.milliseconds)
                remainingSeconds--
            }
            _state.update { it.copy(codeRequestState = VerificationCodeRequestState.Available) }
        }
    }

    /** 인증번호 필드 옆에 표시되는 유효시간(180초) 카운트다운. 발송 성공 시에만 시작한다. */
    private fun startCodeExpiryCountdown() {
        codeExpiryJob?.cancel()
        codeExpiryJob = viewModelScope.launch {
            var remainingSeconds = CODE_EXPIRY_SECONDS
            while (remainingSeconds > 0) {
                _state.update { it.copy(codeExpiresInSeconds = remainingSeconds) }
                delay(1_000.milliseconds)
                remainingSeconds--
            }
            _state.update { it.copy(codeExpiresInSeconds = 0) }
        }
    }

    companion object {
        private const val RESEND_COOLDOWN_SECONDS = 30
        private const val CODE_EXPIRY_SECONDS = 180
    }
}
