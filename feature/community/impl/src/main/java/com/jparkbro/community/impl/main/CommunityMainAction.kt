package com.jparkbro.community.impl.main

sealed interface CommunityMainAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : CommunityMainAction

    data object OnBackClick : Navigation
    data object OnWriteClick : Navigation
    data class OnPostClick(val postId: Long) : Navigation
    data class OnFilterClick(val filter: CommunityPostFilter) : CommunityMainAction
    data class OnSpoilerVisibleChange(val isVisible: Boolean) : CommunityMainAction
    data object OnLoadMorePosts : CommunityMainAction
    data object OnRetryClick : CommunityMainAction
}
