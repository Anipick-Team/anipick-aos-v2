package com.jparkbro.core.model.pagination

data class Cursor(
    val lastId: Long? = null,
    val sort: String? = null,
    val lastValue: String? = null,
)
