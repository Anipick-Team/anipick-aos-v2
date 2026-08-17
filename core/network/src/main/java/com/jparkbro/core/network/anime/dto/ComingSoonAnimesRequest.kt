package com.jparkbro.core.network.anime.dto

import kotlinx.serialization.Serializable

/**
 * Home Detail "공개예정" 화면 커서 페이지네이션 요청. [sort]는 "latest"/"popularity"/"startDate" 중 하나이고,
 * 정렬 UI가 아직 없어서 지금은 항상 null(서버 기본 정렬)로 보낸다. [size]는 서버 스펙상 18로 고정.
 */
@Serializable
data class ComingSoonAnimesRequest(
    val sort: String? = null,
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Long = 18,
)
