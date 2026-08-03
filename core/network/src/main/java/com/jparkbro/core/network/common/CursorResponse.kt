package com.jparkbro.core.network.common

import com.jparkbro.core.model.pagination.Cursor
import kotlinx.serialization.Serializable

@Serializable
data class CursorResponse(
    val lastId: Long? = null,
    val sort: String? = null,
    val lastValue: String? = null,
)

fun CursorResponse.toCursor(): Cursor = Cursor(
    lastId = lastId,
    sort = sort,
    lastValue = lastValue,
)
