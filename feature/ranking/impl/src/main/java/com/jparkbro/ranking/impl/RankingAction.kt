package com.jparkbro.ranking.impl

import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Season

sealed interface RankingAction {
    data class OnRankingTypeSelected(val rankingType: RankingType) : RankingAction
    data class OnFilterChipClick(val filterType: FilterType) : RankingAction
    data object OnFilterSheetDismiss : RankingAction
    data class OnAnimeFilterConfirm(
        val year: Int?,
        val season: Season?,
        val genre: Genre?
    ) : RankingAction
    data object OnLoadMore : RankingAction
    data object OnRetryClick : RankingAction
    data object OnMetadataRetryClick : RankingAction
    data object OnSearchClick : RankingAction
    data class OnAnimeClick(val animeId: Long) : RankingAction
}
