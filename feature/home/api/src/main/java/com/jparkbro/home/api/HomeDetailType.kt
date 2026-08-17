package com.jparkbro.home.api

import kotlinx.serialization.Serializable

/**
 * HomeDetail(전체보기) 화면이 어떤 섹션에서 왔는지를 나타낸다. 네비게이션 인자로 그대로 실려가야
 * 해서 [HomeNavKey.Detail]에 직접 들고 있으며, 화면 쪽에서는 이 타입으로 분기해서
 * 데이터 로딩과 상단 헤더 컴포넌트만 바꿔 끼우고 나머지 레이아웃(애니 목록)은 공유한다.
 */
@Serializable
sealed interface HomeDetailType {

    /**
     * 오늘의 추천작 전체보기.
     * [basedOnAnimeId]가 null이면 취향 기반(오늘의 추천작), 값이 있으면 해당 애니 기반
     * (최근 찾아보신 OOO과 비슷한 작품) 추천이다 — 두 섹션 모두 같은 화면을 쓰되 상단
     * 헤더 문구만 달라진다.
     */
    @Serializable
    data class Recommendation(val basedOnAnimeId: Long? = null) : HomeDetailType

    /** 요일별 신작 전체보기. */
    @Serializable
    data object Weekly : HomeDetailType

    /** 최근 리뷰 전체보기 — 다른 타입과 달리 애니 카드 목록이 아닌 리뷰 목록을 보여준다. */
    @Serializable
    data object Review : HomeDetailType

    /** 공개 예정 전체보기. */
    @Serializable
    data object ComingSoon : HomeDetailType
}
