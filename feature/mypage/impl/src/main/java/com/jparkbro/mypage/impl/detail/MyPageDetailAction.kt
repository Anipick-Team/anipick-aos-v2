package com.jparkbro.mypage.impl.detail

sealed interface MyPageDetailAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : MyPageDetailAction

    data object OnBackClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnPersonClick(val personId: Long) : Navigation
    data class OnMyContentTabSelected(val tab: MyContentTab) : MyPageDetailAction
    data class OnPostClick(val postId: Long) : Navigation
    data class OnCommentClick(val postId: Long) : Navigation
    data object OnLoadMore : MyPageDetailAction
    data object OnRetryClick : MyPageDetailAction
}
