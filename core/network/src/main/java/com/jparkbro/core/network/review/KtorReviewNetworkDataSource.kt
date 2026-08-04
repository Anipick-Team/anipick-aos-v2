package com.jparkbro.core.network.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.post
import io.ktor.client.HttpClient

class KtorReviewNetworkDataSource(
    private val httpClient: HttpClient,
) : ReviewNetworkDataSource {

    override suspend fun submitReviews(reviews: List<ReviewItem>): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "reviews/bulk",
            body = reviews,
        )
    }
}
