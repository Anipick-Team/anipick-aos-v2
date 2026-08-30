package com.jparkbro.catalog.impl.series

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor

data class CatalogSeriesState(
    val animeId: Long = 0L,
    /** 헤더에 쓰는 기준 애니 제목 - "OOO의 시리즈에요!". 시리즈 조회 API 응답에 없어서 진입 시점에 그대로 받는다. */
    val animeTitle: String = "",
    val count: Int = 0,
    val animes: List<Anime> = emptyList(),
    /** 다음 페이지 요청용 커서. 마지막 페이지까지 불러왔으면 null. */
    val cursor: Cursor? = null,
    /** 마지막 페이지까지 다 불러왔는지 - true면 [cursor]가 있어도 더 요청하지 않는다. */
    val endReached: Boolean = false,
    /** 다음 페이지를 불러오는 중인지 - [isLoading]과 별개로 스켈레톤 없이 이어붙이는 로딩. */
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
