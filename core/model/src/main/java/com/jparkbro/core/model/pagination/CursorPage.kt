package com.jparkbro.core.model.pagination

/**
 * 커서 기반 페이지네이션으로 목록만 내려주는 응답들의 공통 모양(랭킹 애니, 최근 리뷰 피드 등).
 * 서버가 같이 내려주는 count는 어디서도 실제로 안 읽어서 여기엔 없다.
 */
data class CursorPage<T>(
    val cursor: Cursor,
    val items: List<T> = emptyList(),
)
