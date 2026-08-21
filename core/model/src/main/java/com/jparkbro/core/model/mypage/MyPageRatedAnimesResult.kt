package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.review.Review

data class MyPageRatedAnimesResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val reviews: List<Review>? = null,
)
