package com.jparkbro.core.model.anime

/** 애니메이션 한 편에 대한 평점. 평가 화면의 진행 중인 상태이자, 서버에 그대로 제출되는 항목이기도 하다. */
data class AnimeRating(
    val animeId: Long,
    val rating: Float,
)
