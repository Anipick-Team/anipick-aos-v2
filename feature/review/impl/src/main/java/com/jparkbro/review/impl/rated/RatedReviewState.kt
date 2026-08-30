package com.jparkbro.review.impl.rated

import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.model.review.ReviewSort

data class RatedReviewState(
    val totalCount: Int = 0,
    val reviews: List<Review> = emptyList(),
    val ratedSort: ReviewSort = ReviewSort.LATEST,
    /** true면 리뷰(글)를 남긴 것만, false면 평점만 남긴 것도 포함. */
    val reviewOnly: Boolean = false,
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    /** 삭제 확인 다이얼로그를 띄울 대상 리뷰 - null이면 다이얼로그를 띄우지 않는다. */
    val deleteTargetReviewId: Long? = null,
)
