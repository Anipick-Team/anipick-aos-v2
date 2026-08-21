package com.jparkbro.auth.impl.preferencesetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupAction
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupState
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator

private const val SEARCH_SKELETON_ITEM_COUNT = 8

@Composable
internal fun PreferenceSetupAnimeList(
    state: PreferenceSetupState,
    onAction: (PreferenceSetupAction) -> Unit,
    lazyListState: LazyListState,
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (state.isSearchError) {
            AniPickEmptyState(
                message = "네트워크 연결을 확인해주세요.",
                onRetryClick = { onAction(PreferenceSetupAction.OnSearchClick) },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        } else if (!state.isSearchLoading && state.animeList.isEmpty()) {
            AniPickEmptyState(
                message = "검색조건에 맞는 결과가 없어요.\n다른 조건으로 검색해보세요.",
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        } else {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = !state.isSearchLoading,
            ) {
                if (state.isSearchLoading) {
                    items(SEARCH_SKELETON_ITEM_COUNT) {
                        PreferenceSetupAnimeItemSkeleton()
                    }
                } else {
                    itemsIndexed(
                        items = state.animeList,
                        key = { _, anime -> anime.animeId ?: anime.hashCode() }
                    ) { index, anime ->
                        val committedRating = state.ratings.find { it.animeId == anime.animeId }?.rating ?: 0f
                        PreferenceSetupAnimeItem(
                            anime = anime,
                            committedRating = committedRating,
                            onSaveRating = { rating -> onAction(PreferenceSetupAction.OnSaveRatingClick(index, rating)) },
                            onCancelRating = { onAction(PreferenceSetupAction.OnCancelRatingClick(index)) },
                        )
                    }
                }
                if (state.isLoadingMore) {
                    item { AniPickLoadMoreIndicator() }
                }
            }
        }
    }
}
