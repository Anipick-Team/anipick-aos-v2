package com.jparkbro.core.network.rating

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.delete
import com.jparkbro.core.network.patch
import com.jparkbro.core.network.post
import com.jparkbro.core.network.rating.dto.RatingRequest
import io.ktor.client.HttpClient

class KtorRatingNetworkDataSource(
    private val httpClient: HttpClient,
) : RatingNetworkDataSource {

    override suspend fun createRating(animeId: Long, rating: Float): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "/rating/$animeId/animes",
            body = RatingRequest(rating = rating),
        )
    }

    override suspend fun updateRating(reviewId: Long, rating: Float): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/rating/$reviewId/animes",
            body = RatingRequest(rating = rating),
        )
    }

    override suspend fun deleteRating(reviewId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/rating/$reviewId/animes")
    }
}
