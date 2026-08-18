package com.jparkbro.core.network.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.post
import com.jparkbro.core.network.review.dto.RecentReviewFeedResponse
import com.jparkbro.core.network.review.dto.ReviewItem
import io.ktor.client.HttpClient

class KtorReviewNetworkDataSource(
    private val httpClient: HttpClient,
) : ReviewNetworkDataSource {

    override suspend fun submitReviews(reviews: List<ReviewItem>): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "/reviews/bulk",
            body = reviews,
        )
    }

    override suspend fun getRecentReviewFeed(lastId: Long?, size: Int): Result<RecentReviewFeedResponse, DataError.Network> {
        return httpClient.get(
            route = "/reviews/recent",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            )
        )
    }
}
