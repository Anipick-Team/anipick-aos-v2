package com.jparkbro.core.data.rating

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.rating.RatingNetworkDataSource

class RatingRepositoryImpl(
    private val ratingNetworkDataSource: RatingNetworkDataSource,
) : RatingRepository {

    override suspend fun createRating(animeId: Long, rating: Float): Result<Unit, DataError.Network> {
        return ratingNetworkDataSource.createRating(animeId, rating)
    }

    override suspend fun updateRating(reviewId: Long, rating: Float): Result<Unit, DataError.Network> {
        return ratingNetworkDataSource.updateRating(reviewId, rating)
    }

    override suspend fun deleteRating(reviewId: Long): Result<Unit, DataError.Network> {
        return ratingNetworkDataSource.deleteRating(reviewId)
    }
}
