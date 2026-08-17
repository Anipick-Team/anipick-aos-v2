package com.jparkbro.core.model.anime

import com.jparkbro.core.model.pagination.Cursor

data class RecommendationResult(
    val referenceAnimeTitle: String? = null,
    /** Home Detail의 커서 페이지네이션 목록에서만 채워진다. Home Main 미리보기에서는 null. */
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList(),
)
