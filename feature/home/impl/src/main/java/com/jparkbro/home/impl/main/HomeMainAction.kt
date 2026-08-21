package com.jparkbro.home.impl.main

sealed interface HomeMainAction {
    data object OnSearchClick : HomeMainAction
    data class OnAnimeClick(val animeId: Long) : HomeMainAction
    data class OnDaySelected(val day: String) : HomeMainAction

    /** 실시간 인기 더보기 — 랭킹 탭으로 이동. */
    data object OnTrendingMoreClick : HomeMainAction

    /** 오늘의 추천작 더보기 — HomeDetail(Recommendation). */
    data object OnRecommendationMoreClick : HomeMainAction

    /** 요일별 신작 더보기 — HomeDetail(Weekly). */
    data object OnWeeklyMoreClick : HomeMainAction

    /** 최근 리뷰 더보기 — HomeDetail(Review). */
    data object OnRecentReviewMoreClick : HomeMainAction

    /** 방영 예정 더보기 — 탐색 탭으로 이동. */
    data object OnUpcomingSeasonMoreClick : HomeMainAction

    /** 최근 확인한 애니 기반 추천 더보기 — HomeDetail(Recommendation(basedOnAnimeId)). */
    data object OnRecentAnimeRecommendationMoreClick : HomeMainAction

    /** 공개 예정 더보기 — HomeDetail(ComingSoon). */
    data object OnComingSoonMoreClick : HomeMainAction

    /** 전체 조회 실패(연결 문제) 화면의 재시도 버튼. */
    data object OnRetryClick : HomeMainAction
}
