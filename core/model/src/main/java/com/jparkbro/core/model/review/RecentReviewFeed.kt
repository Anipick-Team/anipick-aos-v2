package com.jparkbro.core.model.review

import com.jparkbro.core.model.pagination.Cursor

/** HomeDetail "더보기" 화면에서 20개씩 무한스크롤로 불러오는 페이지 단위 결과. */
data class RecentReviewFeed(
    val count: Int,
    val cursor: Cursor,
    val reviews: List<Review>,
)
