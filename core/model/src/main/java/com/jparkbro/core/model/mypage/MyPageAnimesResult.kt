package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor

data class MyPageAnimesResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime>? = null,
)
