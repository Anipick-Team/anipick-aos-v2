package com.jparkbro.review.impl.write

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.review.Review

internal const val REVIEW_CONTENT_MAX_LENGTH = 200

data class ReviewWriteState(
    val animeId: Long = 0L,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    /** `getMyReview(animeId)`로 불러온 기존 리뷰. [Review.reviewId]가 null이면 아직 리뷰를
     *  안 썼다는 뜻이라 작성 모드, 있으면 수정 모드로 화면을 채운다. */
    val currentReview: Review = Review(),
    val rating: Float = 0f,
    val contentState: TextFieldState = TextFieldState(),
    val isSpoiler: Boolean = false,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
) {
    val isEditMode: Boolean
        get() = currentReview.reviewId != null

    val isSubmitEnabled: Boolean
        get() = rating > 0f && !isSubmitting
}
