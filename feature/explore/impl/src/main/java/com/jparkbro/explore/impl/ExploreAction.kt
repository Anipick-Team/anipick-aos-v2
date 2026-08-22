package com.jparkbro.explore.impl

import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Season

sealed interface ExploreAction {
    data class OnTabSelected(val tab: ExploreTab) : ExploreAction
    data class OnSortSelected(val sort: ExploreSort) : ExploreAction
    data class OnFilterChipClick(val filterType: FilterType) : ExploreAction
    data object OnFilterSheetDismiss : ExploreAction
    data class OnFilterConfirm(
        val year: Int?,
        val season: Season?,
        val genres: List<Genre>,
        val genreOp: String?,
        val type: String?,
    ) : ExploreAction
    data object OnYearFilterRemove : ExploreAction
    data object OnSeasonFilterRemove : ExploreAction
    data class OnGenreFilterRemove(val genre: Genre) : ExploreAction
    data object OnTypeFilterRemove : ExploreAction
    data object OnLoadMore : ExploreAction
    data object OnRetryClick : ExploreAction
    data object OnMetadataRetryClick : ExploreAction
    data object OnSearchClick : ExploreAction
    data class OnAnimeClick(val animeId: Long) : ExploreAction
    data object OnCommunitySearchClick : ExploreAction
    data object OnCommunitySearchClearClick : ExploreAction
    data class OnCommunityBoardClick(val board: CommunityBoard) : ExploreAction
    data object OnCommunityLoadMore : ExploreAction
    data object OnCommunityRetryClick : ExploreAction
}
