package com.jparkbro.core.data.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.AnimeRating

interface ReviewRepository {
    suspend fun submitReviews(reviews: List<AnimeRating>): Result<Unit, DataError.Network>
}
