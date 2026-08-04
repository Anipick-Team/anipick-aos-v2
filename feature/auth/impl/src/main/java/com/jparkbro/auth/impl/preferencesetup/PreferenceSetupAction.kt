package com.jparkbro.auth.impl.preferencesetup

import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Season

sealed interface PreferenceSetupAction {
    data object OnSkipClick : PreferenceSetupAction
    data object OnSearchClick : PreferenceSetupAction
    data object OnSearchClearClick : PreferenceSetupAction
    data object OnLoadMore : PreferenceSetupAction
    data class OnFilterChipClick(val filterType: FilterType) : PreferenceSetupAction
    data object OnFilterSheetDismiss : PreferenceSetupAction
    data class OnAnimeFilterConfirm(
        val year: Int?,
        val season: Season?,
        val genre: Genre?,
        val type: String?,
    ) : PreferenceSetupAction
    data class OnSaveRatingClick(val index: Int, val rating: Float) : PreferenceSetupAction
    data class OnCancelRatingClick(val index: Int) : PreferenceSetupAction
    data object OnCompleteClick : PreferenceSetupAction
}
