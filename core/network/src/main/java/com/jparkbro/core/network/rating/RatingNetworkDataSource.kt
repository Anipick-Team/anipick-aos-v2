package com.jparkbro.core.network.rating

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface RatingNetworkDataSource {
    /** 평점 등록 - `POST /rating/{animeId}/animes`. */
    suspend fun createRating(animeId: Long, rating: Float): Result<Unit, DataError.Network>

    /** 평점 수정 - `PATCH /rating/{reviewId}/animes`. */
    suspend fun updateRating(reviewId: Long, rating: Float): Result<Unit, DataError.Network>

    /** 평점 삭제 - `DELETE /rating/{reviewId}/animes`. */
    suspend fun deleteRating(reviewId: Long): Result<Unit, DataError.Network>
}
