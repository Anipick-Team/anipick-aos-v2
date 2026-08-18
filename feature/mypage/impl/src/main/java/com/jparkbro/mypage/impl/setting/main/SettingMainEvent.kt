package com.jparkbro.mypage.impl.setting.main

sealed interface SettingMainEvent {
    data object LogoutConfirmed : SettingMainEvent
}
