package com.jparkbro.home.impl.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import com.jparkbro.home.impl.detail.HomeDetailAction
import com.jparkbro.home.impl.detail.HomeDetailState

/** 요일별 신작 - 요일 선택은 상단에 고정하고, 로딩/빈 결과 상태에서도 계속 보인다. */
@Composable
internal fun WeeklyDetailContent(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DetailHeader(
            state = state,
            onAction = onAction,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        )

        when {
            state.isLoading -> AniPickAnimeGridSkeleton(
                itemCount = 18,
                contentPadding = GRID_CONTENT_PADDING,
            )
            state.animes.isEmpty() -> AniPickEmptyState(
                message = state.error ?: "이 요일에 방영하는 신작이 없어요.",
                onRetryClick = state.error?.let { { onAction(HomeDetailAction.OnRetryClick) } },
            )
            else -> AniPickAnimeInfiniteGrid(
                animes = state.animes,
                onAnimeClick = { anime -> anime.animeId?.let { onAction(HomeDetailAction.OnAnimeClick(it)) } },
                contentPadding = GRID_CONTENT_PADDING,
                onLoadMore = { onAction(HomeDetailAction.OnLoadMore) },
                footer = if (state.isLoadingMore) {
                    { item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() } }
                } else {
                    null
                },
            )
        }
    }
}

/** 요일 선택기가 위쪽 여백을 이미 가지고 있어서 그리드는 위쪽 패딩을 뺀다. */
private val GRID_CONTENT_PADDING = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp)
