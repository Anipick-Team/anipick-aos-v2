package com.jparkbro.mypage.impl.main

import android.net.Uri
import com.jparkbro.mypage.api.MyPageDetailType

sealed interface MyPageMainAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : MyPageMainAction

    data class OnDetailClick(val type: MyPageDetailType) : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnPersonClick(val personId: Long) : Navigation
    data object OnSettingClick : Navigation
    data object OnRatedAnimesClick : Navigation
    data object OnFeedbackClick : Navigation

    /** 포토 피커에서 프로필 이미지를 1장 선택 완료했을 때 - ViewModel이 저장 API를 호출한다. */
    data class OnChangeProfileImage(val image: Uri) : MyPageMainAction
}
