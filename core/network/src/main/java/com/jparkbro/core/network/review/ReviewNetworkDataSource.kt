package com.jparkbro.core.network.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.review.dto.AnimeReviewsRequest
import com.jparkbro.core.network.review.dto.AnimeReviewsResponse
import com.jparkbro.core.network.review.dto.MyReviewResponse
import com.jparkbro.core.network.review.dto.RecentReviewFeedResponse
import com.jparkbro.core.network.review.dto.ReviewItem

interface ReviewNetworkDataSource {
    /** 취향 설정 - 초기 리뷰 일괄 등록 - `POST /reviews/bulk`. */
    suspend fun submitReviews(reviews: List<ReviewItem>): Result<Unit, DataError.Network>

    /** 홈 메인 "최근 리뷰" 더보기 - `GET /reviews/recent`. */
    suspend fun getRecentReviewFeed(lastId: Long?, size: Int): Result<RecentReviewFeedResponse, DataError.Network>

    /** 애니 상세 "리뷰" 탭에서 내가 쓴 리뷰 - `GET /reviews/{animeId}/my-review`. */
    suspend fun getMyReview(animeId: Long): Result<MyReviewResponse, DataError.Network>

    /** 리뷰 작성/수정 - `PATCH /reviews/{animeId}/animes`. */
    suspend fun updateReview(
        animeId: Long,
        content: String,
        rating: Float,
        isSpoiler: Boolean,
    ): Result<Unit, DataError.Network>

    /** 애니 상세 "리뷰" 탭 목록 - `GET /animes/{animeId}/reviews`. */
    suspend fun getAnimeReviews(
        animeId: Long,
        request: AnimeReviewsRequest,
    ): Result<AnimeReviewsResponse, DataError.Network>
}
