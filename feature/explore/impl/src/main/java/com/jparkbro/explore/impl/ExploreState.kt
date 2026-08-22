package com.jparkbro.explore.impl

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Metadata
import com.jparkbro.core.model.metadata.Season
import com.jparkbro.core.model.pagination.Cursor

data class ExploreState(
    val tab: ExploreTab = ExploreTab.ANIME,
    val metadata: Metadata = Metadata(),
    val year: Int? = null,
    val season: Season? = null,
    val genres: List<Genre> = emptyList(),
    val genreOp: String = "OR",
    val type: String? = null,
    val sort: ExploreSort = ExploreSort.POPULARITY,
    val communitySort: ExploreSort = ExploreSort.POPULARITY,
    val activeFilterSheet: FilterType? = null,
    val animes: List<Anime> = emptyList(),
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMetadataError: Boolean = false,
    val communitySearchState: TextFieldState = TextFieldState(),
    val communityBoards: List<CommunityBoard> = emptyList(),
    val communityCursor: Cursor? = null,
    val communityEndReached: Boolean = false,
    val isCommunityLoadingMore: Boolean = false,
    val isCommunityLoading: Boolean = false,
    val communityError: String? = null,
)

/** 탐색 화면 상단 탭 - 작품 탐색(애니 그리드) / 커뮤니티(게시글 목록). */
enum class ExploreTab {
    ANIME, COMMUNITY
}

enum class ExploreSort(val apiValue: String) {
    POPULARITY("popularity"),
    RATING("rating"),
    LATEST("latest"),
}

/** 년도/분기/장르/타입 중 하나라도 선택돼 있으면 true */
internal val ExploreState.hasActiveFilters: Boolean
    get() = year != null || season != null || genres.isNotEmpty() || type != null
