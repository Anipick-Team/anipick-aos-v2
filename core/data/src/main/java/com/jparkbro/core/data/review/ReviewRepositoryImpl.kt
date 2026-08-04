package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.network.review.ReviewItem
import com.jparkbro.core.network.review.ReviewNetworkDataSource

class ReviewRepositoryImpl(
    private val reviewNetworkDataSource: ReviewNetworkDataSource,
) : ReviewRepository {

    override suspend fun submitReviews(reviews: List<AnimeRating>): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.submitReviews(
            reviews.map { ReviewItem(animeId = it.animeId, rating = it.rating) }
        )
    }
}
