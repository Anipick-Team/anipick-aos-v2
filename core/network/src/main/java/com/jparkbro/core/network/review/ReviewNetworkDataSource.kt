package com.jparkbro.core.network.review

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface ReviewNetworkDataSource {
    suspend fun submitReviews(reviews: List<ReviewItem>): Result<Unit, DataError.Network>
}
