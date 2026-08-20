package com.jparkbro.mypage.impl.setting.detail.components

import com.jparkbro.mypage.api.SettingDetailType

internal fun SettingDetailType.title(): String = when (this) {
    SettingDetailType.Nickname -> "닉네임 변경"
    SettingDetailType.Email -> "이메일 변경"
    SettingDetailType.Password -> "비밀번호 변경"
    SettingDetailType.Withdrawal -> "회원 탈퇴"
}
