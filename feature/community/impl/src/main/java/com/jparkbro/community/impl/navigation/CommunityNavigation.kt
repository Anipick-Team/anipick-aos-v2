package com.jparkbro.community.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.community.api.CommunityNavKey
import com.jparkbro.community.impl.detail.CommunityDetailRoot
import com.jparkbro.community.impl.main.CommunityMainRoot
import com.jparkbro.community.impl.write.CommunityWriteRoot
import com.jparkbro.core.navigation.Navigator

/** [CommunityNavKey]의 각 키별 contentKey - 다른 모듈에서도 참조할 수 있게 공개. */
const val COMMUNITY_MAIN_CONTENT_KEY = "CommunityNavKey.Main"
const val COMMUNITY_DETAIL_CONTENT_KEY = "CommunityNavKey.Detail"
const val COMMUNITY_WRITE_CONTENT_KEY = "CommunityNavKey.Write"

fun EntryProviderScope<NavKey>.communityEntry(
    navigator: Navigator,
) {
    entry<CommunityNavKey.Main>(clazzContentKey = { COMMUNITY_MAIN_CONTENT_KEY }) { key ->
        CommunityMainRoot(
            seriesId = key.seriesId,
            title = key.title,
            coverImageUrl = key.coverImageUrl,
            genres = key.genres,
            onBackClick = navigator::goBack,
            onNavigateToPostDetail = { postId -> navigator.navigate(CommunityNavKey.Detail(postId)) },
            onNavigateToPostWrite = {
                navigator.navigate(CommunityNavKey.Write(seriesId = key.seriesId))
            },
        )
    }
    entry<CommunityNavKey.Detail>(clazzContentKey = { COMMUNITY_DETAIL_CONTENT_KEY }) { key ->
        CommunityDetailRoot(
            postId = key.postId,
            onBackClick = navigator::goBack,
            onNavigateToEdit = { seriesId, postId -> navigator.navigate(CommunityNavKey.Write(seriesId = seriesId, postId = postId)) },
        )
    }
    entry<CommunityNavKey.Write>(clazzContentKey = { COMMUNITY_WRITE_CONTENT_KEY }) { key ->
        CommunityWriteRoot(
            seriesId = key.seriesId,
            postId = key.postId,
            onBackClick = navigator::goBack,
            onWriteSuccess = navigator::goBack,
        )
    }
}
