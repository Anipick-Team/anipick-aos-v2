package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.model.review.RecentReviewFeed
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.review.ReviewNetworkDataSource
import com.jparkbro.core.network.review.dto.ReviewItem
import com.jparkbro.core.network.review.dto.toReview

class ReviewRepositoryImpl(
    private val reviewNetworkDataSource: ReviewNetworkDataSource,
) : ReviewRepository {

    override suspend fun submitReviews(reviews: List<AnimeRating>): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.submitReviews(
            reviews.map { ReviewItem(animeId = it.animeId, rating = it.rating) }
        )
    }

    override suspend fun getRecentReviews(): Result<List<Review>, DataError.Network> {
        return reviewNetworkDataSource.getRecentReviews().map { responses ->
            responses.map { it.toReview() }
        }
    }

    override suspend fun getRecentReviewFeed(lastId: Long?, size: Int): Result<RecentReviewFeed, DataError.Network> {
        return reviewNetworkDataSource.getRecentReviewFeed(lastId, size).map { response ->
            RecentReviewFeed(
                count = response.count,
                cursor = response.cursor.toCursor(),
                reviews = response.reviews.map { it.toReview() },
            )
        }
    }
}
