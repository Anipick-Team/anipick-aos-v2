package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.review.Review

interface ReviewRepository {
    suspend fun submitReviews(reviews: List<AnimeRating>): Result<Unit, DataError.Network>
    suspend fun getRecentReviewFeed(lastId: Long? = null, size: Int = 20): Result<CursorPage<Review>, DataError.Network>
}
