package com.jparkbro.core.model.search

import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.studio.Studio

/** `GET /search/studios` 검색 결과. */
data class SearchStudioPage(
    val studios: CursorPage<Studio> = CursorPage(),
    val counts: SearchCounts = SearchCounts(),
)
