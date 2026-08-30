package com.jparkbro.core.model.pagination

/** 커서 기반 페이지네이션 목록 공통 응답 모양 */
data class CursorPage<T>(
    val cursor: Cursor? = null,
    val items: List<T>? = null,
    /** 전체 개수 - 내려주지 않는 엔드포인트도 있다. */
    val count: Int? = null,
)
