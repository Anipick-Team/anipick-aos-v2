package com.jparkbro.catalog.impl.actor

import com.jparkbro.core.model.actor.ActorWork
import com.jparkbro.core.model.pagination.Cursor

data class CatalogActorState(
    val personId: Long = 0L,
    val name: String? = null,
    val profileImageUrl: String? = null,
    val isLiked: Boolean = false,
    val count: Int = 0,
    val works: List<ActorWork> = emptyList(),
    /** 다음 페이지 요청용 커서. 마지막 페이지까지 불러왔으면 null. */
    val cursor: Cursor? = null,
    /** 마지막 페이지까지 다 불러왔는지 - true면 [cursor]가 있어도 더 요청하지 않는다. */
    val endReached: Boolean = false,
    /** 다음 페이지를 불러오는 중인지 - [isLoading]과 별개로 스켈레톤 없이 이어붙이는 로딩. */
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
