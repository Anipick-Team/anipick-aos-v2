package com.jparkbro.review.impl.write

sealed interface ReviewWriteAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : ReviewWriteAction

    data object OnBackClick : Navigation
    data class OnRatingChanged(val rating: Float) : ReviewWriteAction
    data class OnSpoilerToggle(val enabled: Boolean) : ReviewWriteAction
    data object OnGuidelineClick : Navigation
    data object OnSubmitClick : ReviewWriteAction
}
