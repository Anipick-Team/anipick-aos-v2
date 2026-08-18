package com.jparkbro.mypage.impl.setting.detail

sealed interface SettingDetailEvent {
    data object SaveSuccess : SettingDetailEvent
    data object WithdrawSuccess : SettingDetailEvent
}
