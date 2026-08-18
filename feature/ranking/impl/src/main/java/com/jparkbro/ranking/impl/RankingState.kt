package com.jparkbro.ranking.impl

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Metadata
import com.jparkbro.core.model.metadata.Season
import com.jparkbro.core.model.pagination.Cursor

data class RankingState(
    val metadata: Metadata = Metadata(),
    val rankingType: RankingType = RankingType.REAL_TIME,
    val year: Int? = null,
    val season: Season? = null,
    val genre: Genre? = null,
    val activeFilterSheet: FilterType? = null,
    val animes: List<Anime> = emptyList(),
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

enum class RankingType {
    REAL_TIME,
    YEAR_SEASON,
    ALL_TIME,
}

internal val RankingState.yearSeasonLabel: String
    get() = when {
        year == null -> "년도/분기"
        season == null -> "$year"
        else -> "$year/${season.name}"
    }
