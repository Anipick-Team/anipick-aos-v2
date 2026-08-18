package com.jparkbro.mypage.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface MyPageNavKey : NavKey {

    @Serializable
    data object Main : MyPageNavKey

    @Serializable
    data object Detail : MyPageNavKey

    object Setting {
        @Serializable
        data object Main : MyPageNavKey

        @Serializable
        data class Detail(val type: SettingDetailType) : MyPageNavKey
    }
}