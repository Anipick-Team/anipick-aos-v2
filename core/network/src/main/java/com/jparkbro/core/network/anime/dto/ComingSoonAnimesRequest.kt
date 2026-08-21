package com.jparkbro.core.network.anime.dto

import kotlinx.serialization.Serializable

/** 홈 방영예정 더보기 화면 요청 */
@Serializable
data class ComingSoonAnimesRequest(
    val sort: String? = null,
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Long = 18,
)
