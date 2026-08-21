package com.jparkbro.mypage.impl.setting.detail

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.core.ui.validation.EmailPatternValidator
import com.jparkbro.core.ui.validation.PasswordPatternValidator
import com.jparkbro.mypage.api.SettingDetailType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingDetailViewModel(
    private val type: SettingDetailType,
    private val userRepository: UserRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingDetailState(type = type))
    val state: StateFlow<SettingDetailState> = _state.asStateFlow()

    private val _events = Channel<SettingDetailEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadCurrentUser()
        when (type) {
            SettingDetailType.Email -> observeNewEmailValidation()
            SettingDetailType.Password -> {
                observeNewPasswordValidation()
                observePasswordMatch()
            }
            else -> Unit
        }
    }

    fun onAction(action: SettingDetailAction) {
        when (action) {
            SettingDetailAction.OnSaveClick -> {
                if (type == SettingDetailType.Withdrawal) {
                    _state.update { it.copy(showWithdrawDialog = true) }
                } else {
                    save()
                }
            }
            SettingDetailAction.OnWithdrawConfirm -> {
                _state.update { it.copy(showWithdrawDialog = false) }
                save()
            }
            SettingDetailAction.OnWithdrawDialogDismiss -> {
                _state.update { it.copy(showWithdrawDialog = false) }
            }
            SettingDetailAction.OnPasswordVisibilityToggle -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
            SettingDetailAction.OnBackClick -> Unit // 네비게이션만 필요한 액션은 Root에서 처리한다.
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentNickname = userRepository.nickname.first(),
                    currentEmail = userRepository.email.first(),
                )
            }
        }
    }

    private fun observeNewEmailValidation() {
        viewModelScope.launch {
            snapshotFlow { _state.value.emailState.text.toString() }
                .distinctUntilChanged()
                .collect { newEmail ->
                    _state.update { it.copy(isNewEmailValid = EmailPatternValidator.matches(newEmail)) }
                }
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

    private fun save() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    nicknameError = null,
                    emailError = null,
                    emailPasswordError = null,
                    currentPasswordError = null,
                    newPasswordError = null,
                    newPasswordConfirmError = null,
                )
            }

            val current = state.value
            val result = when (type) {
                SettingDetailType.Nickname -> userRepository.updateNickname(
                    nickname = current.nicknameState.text.toString(),
                )
                SettingDetailType.Email -> userRepository.updateEmail(
                    newEmail = current.emailState.text.toString(),
                    password = current.emailPasswordState.text.toString(),
                )
                SettingDetailType.Password -> userRepository.updatePassword(
                    currentPassword = current.currentPasswordState.text.toString(),
                    newPassword = current.newPasswordState.text.toString(),
                    confirmNewPassword = current.checkNewPasswordState.text.toString(),
                )
                SettingDetailType.Withdrawal -> userRepository.withdraw()
            }

            result
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    if (type == SettingDetailType.Withdrawal) {
                        globalSnackbarManager.showSnackbar("회원 탈퇴가 완료되었습니다.")
                        sendEvent(SettingDetailEvent.WithdrawSuccess)
                    } else {
                        sendEvent(SettingDetailEvent.SaveSuccess)
                    }
                }
                .onFailure { error ->
                    Timber.e("설정 저장 실패($type): $error")
                    _state.update { it.copy(isLoading = false) }
                    handleSaveFailure(error)
                }
        }
    }

    private fun handleSaveFailure(error: DataError.Network) {
        if (type == SettingDetailType.Withdrawal) {
            val message = (error as? DataError.Network.Api)?.message ?: "탈퇴하지 못했습니다. 잠시 후 다시 시도해주세요."
            globalSnackbarManager.showSnackbar(message)
            return
        }

        when (error) {
            is DataError.Network.Api -> applyFieldError(error)
            else -> globalSnackbarManager.showSnackbar(error.toDisplayMessage())
        }
    }

    private fun applyFieldError(error: DataError.Network.Api) {
        when (error.code) {
            116, 117, 118 -> _state.update { it.copy(nicknameError = error.message) }
            102, 103, 109 -> _state.update { it.copy(emailError = error.message) }
            106 -> _state.update { it.copy(emailPasswordError = error.message) }
            107 -> _state.update { it.copy(currentPasswordError = error.message) }
            110 -> _state.update { it.copy(newPasswordError = error.message) }
            108 -> _state.update { it.copy(newPasswordConfirmError = error.message) }
            else -> {
                globalSnackbarManager.showSnackbar(error.message ?: "요청을 처리하지 못했습니다.")
            }
        }
    }

    private fun sendEvent(event: SettingDetailEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
