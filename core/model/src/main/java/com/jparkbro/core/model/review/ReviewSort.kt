package com.jparkbro.core.model.review

/** 리뷰 목록 정렬 기준 - 마이페이지 "평가한 작품", 애니 상세 "리뷰" 탭 등에서 공통으로 쓴다. */
enum class ReviewSort(val apiValue: String) {
    LATEST("latest"),
    MOST_LIKED("like"),
    RATING_DESC("ratingDesc"),
    RATING_ASC("ratingAsc"),
}
