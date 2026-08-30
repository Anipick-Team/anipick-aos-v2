package com.jparkbro.community.impl.detail

sealed interface CommunityDetailEvent {
    /** 게시글 삭제 성공 - Root가 이전 화면으로 이동한다. */
    data object DeleteSuccess : CommunityDetailEvent
}
