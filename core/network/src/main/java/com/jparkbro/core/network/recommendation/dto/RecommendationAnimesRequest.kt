package com.jparkbro.core.network.recommendation.dto

import kotlinx.serialization.Serializable

/** 홈 상세 추천 목록 커서 페이지네이션 요청. [size]는 서버 스펙상 18 고정. */
@Serializable
data class RecommendationAnimesRequest(
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Long = 18,
)
