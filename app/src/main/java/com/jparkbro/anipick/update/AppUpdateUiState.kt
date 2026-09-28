package com.jparkbro.anipick.update

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed interface AppUpdateUiState {
    data object Idle : AppUpdateUiState
    data class UpdateAvailable(val info: AppUpdateInfo) : AppUpdateUiState
    data object ReadyToInstall : AppUpdateUiState
}
