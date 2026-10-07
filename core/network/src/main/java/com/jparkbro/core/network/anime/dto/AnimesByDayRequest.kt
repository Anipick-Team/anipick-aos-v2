package com.jparkbro.core.network.anime.dto

import kotlinx.serialization.Serializable

/** 요일별 애니 목록 요청. [day]는 MON, TUE, WED, THU, FRI, SAT, SUN 중 하나. */
@Serializable
data class AnimesByDayRequest(
    val day: String,
    val sort: String? = null,
    val lastId: Long? = null,
    val size: Long = 18,
)
