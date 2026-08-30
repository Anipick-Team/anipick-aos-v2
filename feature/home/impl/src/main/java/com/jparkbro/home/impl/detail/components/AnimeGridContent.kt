package com.jparkbro.home.impl.detail.components

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import com.jparkbro.home.impl.detail.HomeDetailAction
import com.jparkbro.home.impl.detail.HomeDetailState

/** 헤더 + 무한스크롤 애니 그리드 */
@Composable
internal fun AnimeGridContent(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    AniPickAnimeInfiniteGrid(
        animes = state.animes,
        onAnimeClick = { anime -> anime.animeId?.let { onAction(HomeDetailAction.OnAnimeClick(it)) } },
        modifier = modifier,
        onLoadMore = { onAction(HomeDetailAction.OnLoadMore) },
        header = { item(span = { GridItemSpan(maxLineSpan) }) { DetailHeader(state = state, onAction = onAction) } },
        footer = if (state.isLoadingMore) {
            { item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() } }
        } else {
            null
        },
    )
}
