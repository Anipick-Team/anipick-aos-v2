package com.jparkbro.core.network.review.dto

import kotlinx.serialization.Serializable

/** 애니 상세 "리뷰" 탭 커서 페이지네이션 요청. [sort]는 "latest"/"likes"/"ratingDesc"/"ratingAsc" 중 하나. */
@Serializable
data class AnimeReviewsRequest(
    val sort: String? = null,
    val isSpoiler: Boolean? = null,
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Int = 20,
)
