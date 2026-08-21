package com.jparkbro.core.model.actor

import com.jparkbro.core.model.pagination.Cursor

data class ActorDetailPage(
    val personId: Long,
    val name: String? = null,
    val profileImageUrl: String? = null,
    val isLiked: Boolean? = null,
    val count: Int? = null,
    val cursor: Cursor? = null,
    val works: List<ActorWork>? = null,
)
