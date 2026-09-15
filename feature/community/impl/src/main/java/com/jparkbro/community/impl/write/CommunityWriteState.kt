package com.jparkbro.community.impl.write

import androidx.compose.foundation.text.input.TextFieldState

data class CommunityWriteState(
    val seriesId: Long = 0L,
    /** null이면 새 글 작성, 있으면 그 글을 수정하는 모드. */
    val postId: Long? = null,
    val titleState: TextFieldState = TextFieldState(),
    val contentState: TextFieldState = TextFieldState(),
    val titleError: String? = null,
    val isSpoiler: Boolean = false,
    val photos: List<CommunityWritePhoto> = emptyList(),
    val isSubmitEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
) {
    val isEditMode: Boolean
        get() = postId != null
}

internal const val CONTENT_MAX_LENGTH = 1000
internal const val MAX_IMAGE_COUNT = 5
