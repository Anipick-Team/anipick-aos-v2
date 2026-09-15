package com.jparkbro.home.impl.main

sealed interface HomeMainAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : HomeMainAction

    data object OnSearchClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnDaySelected(val day: String) : HomeMainAction

    /** 인스타그램 배너 - 공식 계정으로 이동. */
    data object OnInstagramClick : Navigation

    /** 실시간 인기 더보기 — 랭킹 탭으로 이동. */
    data object OnTrendingMoreClick : Navigation

    /** 오늘의 추천작 더보기 — HomeDetail(Recommendation). */
    data object OnRecommendationMoreClick : Navigation

    /** 요일별 신작 더보기 — HomeDetail(Weekly). */
    data object OnWeeklyMoreClick : Navigation

    /** 최근 리뷰 더보기 — HomeDetail(Review). */
    data object OnRecentReviewMoreClick : Navigation

    /** 방영 예정 더보기 — 탐색 탭으로 이동. */
    data object OnUpcomingSeasonMoreClick : Navigation

    /** 최근 확인한 애니 기반 추천 더보기 — HomeDetail(Recommendation(basedOnAnimeId)). */
    data object OnRecentAnimeRecommendationMoreClick : Navigation

    /** 공개 예정 더보기 — HomeDetail(ComingSoon). */
    data object OnComingSoonMoreClick : Navigation

    /** 전체 조회 실패(연결 문제) 화면의 재시도 버튼. */
    data object OnRetryClick : HomeMainAction
}
