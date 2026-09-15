package com.jparkbro.review.impl.write

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.review.Review

internal const val REVIEW_CONTENT_MAX_LENGTH = 200

data class ReviewWriteState(
    val animeId: Long = 0L,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    /** `getMyReview(animeId)`로 불러온 기존 리뷰 - [Review.content] 유무로 작성/수정 모드 분기 */
    val currentReview: Review = Review(),
    val rating: Float = 0f,
    val contentState: TextFieldState = TextFieldState(),
    val isSpoiler: Boolean = false,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
) {
    val isEditMode: Boolean
        get() = currentReview.content != null

    val isSubmitEnabled: Boolean
        get() = rating > 0f && !isSubmitting
}
