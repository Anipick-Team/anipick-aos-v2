package com.jparkbro.catalog.impl.anime

import com.jparkbro.core.model.community.CommunityBoard

sealed interface CatalogAnimeEvent {
    /** 커뮤니티 탭 선택 시 게시판이 이미 있으면([CommunityBoard.hasBoard] true) 커뮤니티 모듈 화면으로 이동한다.
     *  [board]를 그대로 넘겨서 커뮤니티 화면이 title/coverImageUrl/genres를 다시 조회하지 않고 바로 쓸 수 있게 한다. */
    data class NavigateToCommunity(val board: CommunityBoard) : CatalogAnimeEvent
}
