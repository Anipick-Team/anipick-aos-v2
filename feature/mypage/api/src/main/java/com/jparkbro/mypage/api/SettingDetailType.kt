package com.jparkbro.mypage.api

import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingDetailType {

    @Serializable
    data object Nickname : SettingDetailType

    @Serializable
    data object Email : SettingDetailType

    @Serializable
    data object Password : SettingDetailType

    @Serializable
    data object Withdrawal : SettingDetailType
}
