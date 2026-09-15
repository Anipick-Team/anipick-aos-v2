package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.image.ImageNetworkDataSource
import com.jparkbro.core.network.image.toImageId
import com.jparkbro.core.network.review.ReviewNetworkDataSource
import com.jparkbro.core.network.review.dto.AnimeReviewsRequest
import com.jparkbro.core.network.review.dto.ReviewItem
import com.jparkbro.core.network.review.dto.toReview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ReviewRepositoryImpl(
    private val reviewNetworkDataSource: ReviewNetworkDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource,
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
                items = response.reviews?.map { it.toReview() }?.withProfileImageBytes(),
            )
        }
    }

    /** code 302(리뷰 없음)는 빈 [Review]로 성공 처리 */
    override suspend fun getMyReview(animeId: Long): Result<Review, DataError.Network> {
        val result = reviewNetworkDataSource.getMyReview(animeId)
        val error = (result as? Result.Failure)?.error
        return if (error is DataError.Network.Api && error.code == REVIEW_NOT_FOUND_CODE) {
            Result.Success(Review())
        } else {
            result.map { it.toReview() }
        }
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
                items = response.reviews?.map { it.toReview() }?.withProfileImageBytes(),
            )
        }
    }

    override suspend fun deleteReview(reviewId: Long): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.deleteReview(reviewId)
    }

    override suspend fun likeReview(reviewId: Long): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.likeReview(reviewId)
    }

    override suspend fun unlikeReview(reviewId: Long): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.unlikeReview(reviewId)
    }

    override suspend fun reportReview(reviewId: Long, reportCategory: ReportCategory): Result<Unit, DataError.Network> {
        return reviewNetworkDataSource.reportReview(reviewId, reportCategory.name)
    }

    /** 작성자 프로필 이미지는 인증이 필요해서 URL을 바로 못 쓴다 - id를 뽑아 병렬로 바이트를 채워 넣는다 */
    private suspend fun List<Review>.withProfileImageBytes(): List<Review> = coroutineScope {
        map { review ->
            async {
                val imageId = review.profileImageUrl?.toImageId() ?: return@async review
                val bytes = (imageNetworkDataSource.getImage(imageId) as? Result.Success)?.data
                review.copy(profileImageBytes = bytes)
            }
        }.awaitAll()
    }

    companion object {
        private const val REVIEW_NOT_FOUND_CODE = 302
    }
}
