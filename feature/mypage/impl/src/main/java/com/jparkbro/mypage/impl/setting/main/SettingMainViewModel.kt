package com.jparkbro.mypage.impl.setting.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingMainViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingMainState())
    val state: StateFlow<SettingMainState> = _state.asStateFlow()

    private val _events = Channel<SettingMainEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeUser()
        loadUserSetting()
    }

    private fun observeUser() {
        viewModelScope.launch {
            combine(userRepository.nickname, userRepository.email) { nickname, email ->
                nickname to email
            }.collect { (nickname, email) ->
                _state.update { it.copy(nickname = nickname, email = email) }
            }
        }
    }

    private fun loadUserSetting() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            userRepository.getUserSetting()
                .onSuccess { setting ->
                    _state.update {
                        it.copy(
                            provider = setting.provider,
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    globalSnackbarManager.showSnackbar("설정 정보를 불러오지 못했습니다")
                }
        }
    }

    fun onAction(action: SettingMainAction) {
        when (action) {
            SettingMainAction.OnLogoutClick -> _state.update { it.copy(showLogoutDialog = true) }
            SettingMainAction.OnLogoutDialogDismiss -> _state.update { it.copy(showLogoutDialog = false) }
            SettingMainAction.OnLogoutConfirm -> logout()
            else -> Unit // 네비게이션/외부 링크 액션은 Root에서 처리한다.
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(showLogoutDialog = false) }
            authRepository.clearLocalData()
            sendEvent(SettingMainEvent.LogoutConfirmed)
        }
    }

    private fun sendEvent(event: SettingMainEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
