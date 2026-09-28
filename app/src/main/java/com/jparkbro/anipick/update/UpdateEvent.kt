package com.jparkbro.anipick.update

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed interface UpdateEvent {
    /** Activity가 startUpdateFlowForResult()를 직접 호출해야 해서 Event로 전달 */
    data class StartUpdateFlow(val info: AppUpdateInfo) : UpdateEvent
    /** 다운로드 완료 후 재시작 확정 - appUpdateManager.completeUpdate() 호출 요청 */
    data object CompleteUpdate : UpdateEvent
}
