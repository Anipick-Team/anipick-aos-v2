package com.jparkbro.home.api

import kotlinx.serialization.Serializable

/** HomeDetail(전체보기) 화면이 어떤 섹션에서 왔는지 나타낸다. 데이터 로딩과 헤더만 바뀌고 나머지 레이아웃은 공유 */
@Serializable
sealed interface HomeDetailType {

    /** 오늘의 추천작 전체보기. [basedOnAnimeId] null: 취향 기반, 있음: 해당 애니 기반 추천 */
    @Serializable
    data class Recommendation(val basedOnAnimeId: Long? = null) : HomeDetailType

    /** 요일별 신작 전체보기. */
    @Serializable
    data object Weekly : HomeDetailType

    /** 공개 예정 전체보기. */
    @Serializable
    data object ComingSoon : HomeDetailType
}
