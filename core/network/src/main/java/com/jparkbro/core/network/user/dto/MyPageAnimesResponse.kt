package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 마이페이지 보기 상태별(찜/보는 중/완료) 애니 목록 응답 */
@Serializable
data class MyPageAnimesResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<MyPageAnimeResponse>? = null,
)

@Serializable
data class MyPageAnimeResponse(
    val animeId: Long? = null,
    val userAnimeStatusId: Int? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val isAdult: Boolean? = null,
)

fun MyPageAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    userAnimeStatusId = userAnimeStatusId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
