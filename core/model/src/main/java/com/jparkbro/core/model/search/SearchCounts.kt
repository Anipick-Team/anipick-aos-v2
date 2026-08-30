package com.jparkbro.core.model.search

/** 검색어 하나에 대한 리소스별 전체 결과 수 - 세 탭(작품/인물/제작사)이 같은 값을 공유한다. */
data class SearchCounts(
    val animeCount: Int? = null,
    val actorCount: Int? = null,
    val studioCount: Int? = null,
)
