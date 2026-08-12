package com.jparkbro.core.network.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface ReviewNetworkDataSource {
    suspend fun submitReviews(reviews: List<ReviewItem>): Result<Unit, DataError.Network>
    suspend fun getRecentReviews(): Result<List<RecentReviewResponse>, DataError.Network>
    suspend fun getRecentReviewFeed(lastId: Long?, size: Int): Result<RecentReviewFeedResponse, DataError.Network>
}
