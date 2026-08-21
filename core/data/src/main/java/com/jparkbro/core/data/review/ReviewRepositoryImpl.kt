package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.review.ReviewNetworkDataSource
import com.jparkbro.core.network.review.dto.AnimeReviewsRequest
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

    override suspend fun getRecentReviewFeed(lastId: Long?, size: Int): Result<CursorPage<Review>, DataError.Network> {
        return reviewNetworkDataSource.getRecentReviewFeed(lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.reviews?.map { it.toReview() },
            )
        }
    }

    override suspend fun getMyReview(animeId: Long): Result<Review, DataError.Network> {
        return reviewNetworkDataSource.getMyReview(animeId).map { it.toReview() }
    }

    override suspend fun updateReview(
        animeId: Long,
        content: String,
        rating: Float,
        isSpoiler: Boolean,
    ): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.updateReview(animeId, content, rating, isSpoiler)
    }

    override suspend fun getAnimeReviews(
        animeId: Long,
        sort: String?,
        isSpoiler: Boolean?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<CursorPage<Review>, DataError.Network> {
        val request = AnimeReviewsRequest(
            sort = sort,
            isSpoiler = isSpoiler,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        )
        return reviewNetworkDataSource.getAnimeReviews(animeId, request).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.reviews?.map { it.toReview() },
            )
        }
    }
}
