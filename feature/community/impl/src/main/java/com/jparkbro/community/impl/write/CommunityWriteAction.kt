package com.jparkbro.community.impl.write

import android.net.Uri

sealed interface CommunityWriteAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : CommunityWriteAction

    data object OnBackClick : Navigation
    data object OnSubmitClick : CommunityWriteAction
    data class OnSpoilerToggle(val isSpoiler: Boolean) : CommunityWriteAction
    data class OnImagesAdd(val uris: List<Uri>) : CommunityWriteAction
    data class OnPhotoRemove(val key: Any) : CommunityWriteAction
}
