package com.jparkbro.explore.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import com.jparkbro.core.ui.effect.LoadMoreEffect
import com.jparkbro.explore.impl.ExploreAction
import com.jparkbro.explore.impl.ExploreState
import com.jparkbro.explore.impl.ExploreTab

/** 애니/커뮤니티 탭 콘텐츠 - ExploreScreen의 탭 분기. */
@Composable
internal fun ColumnScope.ExploreTabContent(
    state: ExploreState,
    onAction: (ExploreAction) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    gridContentPadding: PaddingValues,
) {
    when (state.tab) {
        ExploreTab.ANIME -> {
            if (state.isLoading) {
                AniPickAnimeGridSkeleton(
                    modifier = Modifier.weight(1f),
                    itemCount = 18,
                    contentPadding = gridContentPadding,
                )
            } else if (state.animes.isEmpty()) {
                AniPickEmptyState(
                    message = state.error ?: "검색조건에 맞는 결과가 없어요.\n다른 조건으로 검색해보세요.",
                    onRetryClick = state.error?.let { { onAction(ExploreAction.OnRetryClick) } },
                    modifier = Modifier.weight(1f),
                )
            } else {
                AniPickAnimeInfiniteGrid(
                    animes = state.animes,
                    onAnimeClick = { anime -> anime.animeId?.let { onAction(ExploreAction.OnAnimeClick(it)) } },
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(nestedScrollConnection),
                    onLoadMore = { onAction(ExploreAction.OnLoadMore) },
                    footer = if (state.isLoadingMore) {
                        { item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() } }
                    } else {
                        null
                    },
                    contentPadding = gridContentPadding,
                )
            }
        }

        ExploreTab.COMMUNITY -> {
            val communityListState = rememberLazyListState()
            LoadMoreEffect(state = communityListState, onLoadMore = { onAction(ExploreAction.OnCommunityLoadMore) })

            if (state.isCommunityLoading) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(nestedScrollConnection),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(COMMUNITY_SKELETON_ITEM_COUNT) { ExploreCommunityBoardItemSkeleton() }
                }
            } else if (state.communityBoards.isEmpty()) {
                AniPickEmptyState(
                    message = state.communityError ?: "표시할 커뮤니티가 없습니다.",
                    onRetryClick = state.communityError?.let { { onAction(ExploreAction.OnCommunityRetryClick) } },
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(nestedScrollConnection),
                )
            } else {
                LazyColumn(
                    state = communityListState,
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(nestedScrollConnection),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.communityBoards, key = { it.seriesId ?: 0L }) { board ->
                        ExploreCommunityBoardItem(
                            board = board,
                            onClick = { onAction(ExploreAction.OnCommunityBoardClick(board)) },
                        )
                    }
                    if (state.isCommunityLoadingMore) {
                        item { AniPickLoadMoreIndicator() }
                    }
                }
            }
        }
    }
}

private const val COMMUNITY_SKELETON_ITEM_COUNT = 6
