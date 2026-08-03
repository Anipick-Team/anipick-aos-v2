package com.jparkbro.auth.impl.preferencesetup

import com.jparkbro.core.model.metadata.FilterType

sealed interface PreferenceSetupAction {
    data object OnSkipClick : PreferenceSetupAction
    data object OnSearchClick : PreferenceSetupAction
    data object OnSearchClearClick : PreferenceSetupAction
    data object OnLoadMore : PreferenceSetupAction
    data class OnFilterChipClick(val filterType: FilterType) : PreferenceSetupAction
    data class OnSaveRatingClick(val index: Int, val rating: Float) : PreferenceSetupAction
    data class OnCancelRatingClick(val index: Int) : PreferenceSetupAction
    data object OnCompleteClick : PreferenceSetupAction
}
