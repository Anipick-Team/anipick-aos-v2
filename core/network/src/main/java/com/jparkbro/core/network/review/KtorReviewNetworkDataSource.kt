package com.jparkbro.core.network.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.patch
import com.jparkbro.core.network.post
import com.jparkbro.core.network.review.dto.AnimeReviewsRequest
import com.jparkbro.core.network.review.dto.AnimeReviewsResponse
import com.jparkbro.core.network.review.dto.MyReviewResponse
import com.jparkbro.core.network.review.dto.RecentReviewFeedResponse
import com.jparkbro.core.network.review.dto.ReviewItem
import com.jparkbro.core.network.review.dto.UpdateReviewRequest
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

    override suspend fun getMyReview(animeId: Long): Result<MyReviewResponse, DataError.Network> {
        return httpClient.get(route = "/reviews/$animeId/my-review")
    }

    override suspend fun updateReview(
        animeId: Long,
        content: String,
        rating: Float,
        isSpoiler: Boolean,
    ): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/reviews/$animeId/animes",
            body = UpdateReviewRequest(content = content, rating = rating, isSpoiler = isSpoiler),
        )
    }

    override suspend fun getAnimeReviews(
        animeId: Long,
        request: AnimeReviewsRequest,
    ): Result<AnimeReviewsResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/$animeId/reviews",
            queryParameters = mapOf(
                "sort" to request.sort,
                "isSpoiler" to request.isSpoiler,
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
        )
    }
}
