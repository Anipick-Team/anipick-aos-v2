package com.jparkbro.anipick.update

sealed interface UpdateAction {
    data object OnUpdateConfirmed : UpdateAction   // 유저 "업데이트" 수락
    data object OnUpdateDismissed : UpdateAction   // 유저 "나중에" 선택
    data object OnInstallConfirmed : UpdateAction  // 다운로드 완료 후 "재시작" 수락
    data object OnInstallDismissed : UpdateAction  // 다운로드 완료 후 "나중에" 선택
}
