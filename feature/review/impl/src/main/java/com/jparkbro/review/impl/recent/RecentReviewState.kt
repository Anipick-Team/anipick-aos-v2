package com.jparkbro.review.impl.recent

import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.review.Review

data class RecentReviewState(
    val reviews: List<Review> = emptyList(),
    /** 다음 페이지 요청용 커서. 마지막 페이지까지 불러왔으면 null. */
    val cursor: Cursor? = null,
    /** 마지막 페이지까지 다 불러왔는지 - true면 [cursor]가 있어도 더 요청하지 않는다. */
    val endReached: Boolean = false,
    /** 다음 페이지를 불러오는 중인지 - [isLoading]과 별개로 스켈레톤 없이 이어붙이는 로딩. */
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    /** 신고 다이얼로그를 띄울 대상 리뷰 - null이면 다이얼로그를 띄우지 않는다. */
    val reportTargetReviewId: Long? = null,
    val reportCategory: ReportCategory? = null,
    /** 삭제 확인 다이얼로그를 띄울 대상 리뷰 - null이면 다이얼로그를 띄우지 않는다. */
    val deleteTargetReviewId: Long? = null,
)
