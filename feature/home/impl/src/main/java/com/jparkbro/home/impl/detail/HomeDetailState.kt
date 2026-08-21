package com.jparkbro.home.impl.detail

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.home.api.HomeDetailType

data class HomeDetailState(
    val type: HomeDetailType,
    val animes: List<Anime> = emptyList(),
    /** Recommendation(basedOnAnimeId != null)일 때 헤더에 쓰는 기준 애니 제목. */
    val referenceAnimeTitle: String? = null,
    /** Recommendation(basedOnAnimeId == null)일 때 헤더에 쓰는 닉네임. */
    val nickname: String? = null,
    /** Weekly일 때만 쓰는 선택된 요일. */
    val selectedDayOfWeek: String = "월",
    /** ComingSoon일 때만 쓰는 정렬 기준(latest/popularity/startDate). null이면 최신순으로 취급. */
    val sort: String? = null,
    /** Recommendation/ComingSoon 무한스크롤 다음 페이지 요청용 커서. 마지막 페이지까지 불러왔으면 null. */
    val cursor: Cursor? = null,
    /** 마지막 페이지까지 다 불러왔는지 - true면 [cursor]가 있어도 더 요청하지 않는다. */
    val endReached: Boolean = false,
    /** 다음 페이지를 불러오는 중인지 - [isLoading]과 별개로 스켈레톤 없이 이어붙이는 로딩. */
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
