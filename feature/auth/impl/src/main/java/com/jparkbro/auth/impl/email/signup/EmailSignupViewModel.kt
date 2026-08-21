package com.jparkbro.auth.impl.email.signup

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.core.ui.validation.EmailPatternValidator
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

class EmailSignupViewModel(
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(EmailSignupState())
    val state: StateFlow<EmailSignupState> = _state.asStateFlow()

    private val _events = Channel<EmailSignupEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeEmailValidation()
        observePasswordValidation()
        observeSignupButtonEnabled()
    }

    fun onAction(action: EmailSignupAction) {
        when (action) {
            EmailSignupAction.OnPasswordVisibilityToggle -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
            EmailSignupAction.OnAgreeAllToggle -> {
                val agreeAll = !_state.value.isAgreeAll
                _state.update {
                    it.copy(
                        isAgeChecked = agreeAll,
                        isTermsOfServiceChecked = agreeAll,
                        isPrivacyPolicyChecked = agreeAll,
                    )
                }
            }
            EmailSignupAction.OnAgeToggle -> {
                _state.update { it.copy(isAgeChecked = !it.isAgeChecked) }
            }
            EmailSignupAction.OnTermsOfServiceToggle -> {
                _state.update { it.copy(isTermsOfServiceChecked = !it.isTermsOfServiceChecked) }
            }
            EmailSignupAction.OnPrivacyPolicyToggle -> {
                _state.update { it.copy(isPrivacyPolicyChecked = !it.isPrivacyPolicyChecked) }
            }
            EmailSignupAction.OnSignUpClick -> signUp()

            EmailSignupAction.OnBackClick,
            EmailSignupAction.OnTermsOfServiceDetailClick,
            EmailSignupAction.OnPrivacyPolicyDetailClick -> Unit
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

    private fun observePasswordValidation() {
        viewModelScope.launch {
            snapshotFlow { _state.value.passwordState.text.toString() }
                .distinctUntilChanged()
                .collect { password ->
                    val isValid = PasswordPatternValidator.isValidPassword(password).isValidPassword
                    _state.update { it.copy(isPasswordValid = isValid) }
                }
        }
    }

    private fun observeSignupButtonEnabled() {
        viewModelScope.launch {
            combine(
                snapshotFlow { _state.value.emailState.text.toString() },
                _state.map { it.isPasswordValid }.distinctUntilChanged(),
                _state.map { it.isAgeChecked && it.isTermsOfServiceChecked && it.isPrivacyPolicyChecked }.distinctUntilChanged(),
                _state.map { it.isLoading }.distinctUntilChanged(),
            ) { email, isPasswordValid, isAllAgreed, isLoading ->
                email.isNotEmpty() && EmailPatternValidator.matches(email) &&
                    isPasswordValid && isAllAgreed && !isLoading
            }
                .distinctUntilChanged()
                .collect { isSignupEnabled ->
                    _state.update { it.copy(isSignupEnabled = isSignupEnabled) }
                }
        }
    }

    private fun signUp() {
        val email = _state.value.emailState.text.toString()
        val password = _state.value.passwordState.text.toString()
        val termsAndConditions = _state.value.isAgreeAll

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, emailError = null, passwordError = null, termsError = null)
            }

            authRepository.signUpWithEmail(email, password, termsAndConditions)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(EmailSignupEvent.NavigateToPreferenceSetup)
                }
                .onFailure { error ->
                    Timber.e("이메일 회원가입 실패: $error")
                    _state.update { it.copy(isLoading = false) }
                    handleSignupFailure(error)
                }
        }
    }

    private fun handleSignupFailure(error: DataError.Network) {
        when (error) {
            is DataError.Network.Api -> {
                when (error.code) {
                    109 -> _state.update { it.copy(emailError = error.message) } // 이미 가입된 이메일
                    110 -> _state.update { it.copy(passwordError = error.message) } // 취약한 비밀번호
                    111 -> _state.update { it.copy(termsError = error.message) } // 약관 미동의
                    else -> globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
            }
            else -> globalSnackbarManager.showSnackbar(error.toDisplayMessage())
        }
    }
}
