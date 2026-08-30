package com.jparkbro.ranking.impl

import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Season

sealed interface RankingAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : RankingAction

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
    data object OnSearchClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
}
