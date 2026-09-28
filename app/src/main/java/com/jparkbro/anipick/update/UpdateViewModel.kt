package com.jparkbro.anipick.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.requestAppUpdateInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateViewModel(
    private val appUpdateManager: AppUpdateManager,
) : ViewModel() {

    private val _state = MutableStateFlow(UpdateState())
    val state = _state.asStateFlow()

    private val _events = Channel<UpdateEvent>()
    val events = _events.receiveAsFlow()

    // 다운로드 진행 상태 감지 listener - onCleared()에서 해제
    private val installStateListener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            _state.update { it.copy(appUpdate = AppUpdateUiState.ReadyToInstall) }
        }
    }

    init {
        appUpdateManager.registerListener(installStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        appUpdateManager.unregisterListener(installStateListener)
    }

    /** 앱 시작 시 업데이트 유무 확인 */
    fun checkForUpdate() {
        viewModelScope.launch {
            runCatching { appUpdateManager.requestAppUpdateInfo() }
                .onSuccess { info ->
                    val isAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    if (isAvailable && info.isFlexibleUpdateAllowed) {
                        _state.update { it.copy(appUpdate = AppUpdateUiState.UpdateAvailable(info)) }
                    }
                }
        }
    }

    /** onResume 시 호출 - 백그라운드 다운로드 완료 여부 재확인 */
    fun checkDownloadedUpdate() {
        viewModelScope.launch {
            runCatching { appUpdateManager.requestAppUpdateInfo() }
                .onSuccess { info ->
                    if (info.installStatus() == InstallStatus.DOWNLOADED) {
                        _state.update { it.copy(appUpdate = AppUpdateUiState.ReadyToInstall) }
                    }
                }
        }
    }

    fun onAction(action: UpdateAction) {
        when (action) {
            UpdateAction.OnUpdateConfirmed -> {
                val current = _state.value.appUpdate
                if (current is AppUpdateUiState.UpdateAvailable) {
                    viewModelScope.launch { _events.send(UpdateEvent.StartUpdateFlow(current.info)) }
                }
                _state.update { it.copy(appUpdate = AppUpdateUiState.Idle) }
            }
            UpdateAction.OnUpdateDismissed -> {
                _state.update { it.copy(appUpdate = AppUpdateUiState.Idle) }
            }
            UpdateAction.OnInstallConfirmed -> {
                viewModelScope.launch { _events.send(UpdateEvent.CompleteUpdate) }
                _state.update { it.copy(appUpdate = AppUpdateUiState.Idle) }
            }
            UpdateAction.OnInstallDismissed -> {
                _state.update { it.copy(appUpdate = AppUpdateUiState.Idle) }
            }
        }
    }
}
