package com.jparkbro.core.model.pagination

/** 커서 기반 페이지네이션 목록 공통 응답 모양 (count 제외) */
data class CursorPage<T>(
    val cursor: Cursor? = null,
    val items: List<T>? = null,
)
