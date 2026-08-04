package com.jparkbro.auth.impl.password.verification

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.core.ui.validation.EmailPatternValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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

    init {
        observeEmailValidation()
        observeVerifyEnabled()
    }

    fun onAction(action: PasswordVerificationAction) {
        when (action) {
            PasswordVerificationAction.OnRequestVerificationCodeClick -> requestVerificationCode()
            PasswordVerificationAction.OnVerifyClick -> verifyCode()
            PasswordVerificationAction.OnAlertDismiss -> {
                _state.update { it.copy(showSnsLoginAlert = false) }
            }
            PasswordVerificationAction.OnSnsLoginClick -> {
                _state.update { it.copy(showSnsLoginAlert = false) }
                viewModelScope.launch { _events.send(PasswordVerificationEvent.NavigateToLogin) }
            }
            PasswordVerificationAction.OnBackClick -> Unit
        }
    }

    private fun observeEmailValidation() {
        viewModelScope.launch {
            snapshotFlow { _state.value.emailState.text.toString() }
                .distinctUntilChanged()
                .collect { email ->
                    val isEmailValid = email.isNotEmpty() && EmailPatternValidator.matches(email)
                    val emailError = if (email.isNotEmpty() && !isEmailValid) {
                        "올바른 이메일 형식이 아닙니다."
                    } else {
                        null
                    }
                    _state.update { it.copy(emailError = emailError, isEmailValid = isEmailValid) }
                    resetCodeSessionIfActive()
                }
        }
    }

    /**
     * 이미 인증번호를 받은 상태(발송 성공 이후)에서 이메일을 수정하면, 화면에 남아있는 카운트다운/재발송
     * 상태/입력했던 인증번호가 전부 "예전 이메일" 기준이라 오해를 살 수 있어서 초기화한다.
     * 아직 한 번도 발송 안 한 상태(Idle)에서 타이핑하는 중이면 아무 것도 하지 않는다.
     */
    private fun resetCodeSessionIfActive() {
        val current = _state.value
        val hasActiveCodeSession = current.codeExpiresInSeconds != null ||
            current.codeRequestState !is VerificationCodeRequestState.Idle
        if (!hasActiveCodeSession) return

        codeExpiryJob?.cancel()
        resendCooldownJob?.cancel()
        current.codeState.setTextAndPlaceCursorAtEnd("")
        _state.update {
            it.copy(
                codeRequestState = VerificationCodeRequestState.Idle,
                codeExpiresInSeconds = null,
                codeError = null,
            )
        }
    }

    private fun observeVerifyEnabled() {
        viewModelScope.launch {
            combine(
                snapshotFlow { _state.value.emailState.text.toString() },
                snapshotFlow { _state.value.codeState.text.toString() },
                _state.map { it.isLoading }.distinctUntilChanged(),
            ) { email, code, isLoading ->
                email.isNotEmpty() && EmailPatternValidator.matches(email) && code.isNotEmpty() && !isLoading
            }
                .distinctUntilChanged()
                .collect { isVerifyEnabled ->
                    _state.update { it.copy(isVerifyEnabled = isVerifyEnabled) }
                }
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
                    // 실패해도 이미 한 번 시도한 거라 "인증번호 받기"가 아니라 "재발송" 상태로 바로 보낸다.
                    resendCooldownJob?.cancel()
                    _state.update { it.copy(codeRequestState = VerificationCodeRequestState.Available) }
                    handleSendFailure(error)
                }
        }
    }

    private fun handleSendFailure(error: DataError.Network) {
        when (error) {
            is DataError.Network.Api -> {
                when (error.code) {
                    112 -> _state.update { it.copy(emailError = error.message) }
                    113, 114, 115 -> _state.update { it.copy(codeError = error.message) }
                    122 -> _state.update { it.copy(showSnsLoginAlert = true) }
                    else -> globalSnackbarManager.showSnackbar(error.message ?: "알 수 없는 오류가 발생했습니다.")
                }
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
            _state.update {
                it.copy(
                    codeExpiresInSeconds = 0,
                    codeError = "유효 시간이 만료되었습니다. 재발송 후 다시 시도해주세요.",
                )
            }
        }
    }

    companion object {
        private const val RESEND_COOLDOWN_SECONDS = 30
        private const val CODE_EXPIRY_SECONDS = 180
    }
}
