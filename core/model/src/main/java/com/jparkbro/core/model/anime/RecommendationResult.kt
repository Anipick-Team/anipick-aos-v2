package com.jparkbro.core.model.anime

import com.jparkbro.core.model.pagination.Cursor

data class RecommendationResult(
    val referenceAnimeTitle: String? = null,
    /** Home Detail 목록: 값 존재, Home Main 미리보기: null */
    val cursor: Cursor? = null,
    val animes: List<Anime>? = null,
)
