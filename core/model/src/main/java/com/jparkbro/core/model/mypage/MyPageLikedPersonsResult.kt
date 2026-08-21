package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.pagination.Cursor

data class MyPageLikedPersonsResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val persons: List<Actor>? = null,
)
