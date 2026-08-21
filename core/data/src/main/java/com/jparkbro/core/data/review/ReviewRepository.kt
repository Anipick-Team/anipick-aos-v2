package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.review.Review

/** 리뷰 관련 데이터를 읽고 쓰는 인터페이스 */
interface ReviewRepository {
    /** 리뷰 일괄 등록 - `POST /reviews/bulk`. */
    suspend fun submitReviews(reviews: List<AnimeRating>): Result<Unit, DataError.Network>
    /** 최근 리뷰 피드 - `GET /reviews/recent`. */
    suspend fun getRecentReviewFeed(lastId: Long? = null, size: Int = 20): Result<CursorPage<Review>, DataError.Network>

    /** 애니 상세 "리뷰" 탭에서 내가 쓴 리뷰 - `GET /reviews/{animeId}/my-review`. */
    suspend fun getMyReview(animeId: Long): Result<Review, DataError.Network>

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
        sort: String? = null,
        isSpoiler: Boolean? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Review>, DataError.Network>
}
