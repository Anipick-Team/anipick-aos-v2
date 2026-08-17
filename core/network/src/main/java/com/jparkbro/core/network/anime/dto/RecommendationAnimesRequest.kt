package com.jparkbro.core.network.anime.dto

import kotlinx.serialization.Serializable

/**
 * Home Detail의 추천 목록 화면(전체 추천/최근 본 애니 기반 추천 둘 다) 커서 페이지네이션 요청.
 * [size]는 서버 스펙상 18로 고정.
 */
@Serializable
data class RecommendationAnimesRequest(
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Long = 18,
)
