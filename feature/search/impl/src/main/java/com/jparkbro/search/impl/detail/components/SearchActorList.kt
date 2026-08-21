package com.jparkbro.search.impl.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickCountLabel
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.ui.component.AniPickActorCard
import com.jparkbro.core.ui.component.AniPickAnimeCardSkeleton
import com.jparkbro.core.ui.component.AniPickCardBackground
import com.jparkbro.core.ui.component.calculateAnimeGridLayout
import com.jparkbro.core.ui.effect.LoadMoreEffect

private const val SEARCH_ACTOR_GRID_SKELETON_ITEM_COUNT = 18
private val SEARCH_ACTOR_GRID_HORIZONTAL_PADDING = 20.dp
private val SEARCH_ACTOR_GRID_VERTICAL_SPACING = 16.dp
private val EMPTY_STATE_HEIGHT = 360.dp

@Composable
internal fun SearchActorList(
    actors: List<Actor>,
    isLoading: Boolean,
    totalCount: Int,
    onActorClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isLoadingMore: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
    emptyMessage: String? = null,
    onRetryClick: (() -> Unit)? = null,
) {
    BoxWithConstraints(modifier = modifier) {
        val layout = remember(maxWidth) {
            calculateAnimeGridLayout(availableWidth = maxWidth - SEARCH_ACTOR_GRID_HORIZONTAL_PADDING * 2)
        }
        val gridState = rememberLazyGridState()

        if (onLoadMore != null) {
            LoadMoreEffect(state = gridState, threshold = layout.columns + 1, onLoadMore = onLoadMore)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(layout.columns),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = SEARCH_ACTOR_GRID_HORIZONTAL_PADDING,
                end = SEARCH_ACTOR_GRID_HORIZONTAL_PADDING,
                top = SEARCH_ACTOR_GRID_VERTICAL_SPACING,
                bottom = SEARCH_ACTOR_GRID_HORIZONTAL_PADDING,
            ),
            horizontalArrangement = Arrangement.spacedBy(layout.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(SEARCH_ACTOR_GRID_VERTICAL_SPACING),
            userScrollEnabled = !isLoading,
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AniPickCountLabel(count = totalCount, unit = "명")
            }

            if (isLoading) {
                items(SEARCH_ACTOR_GRID_SKELETON_ITEM_COUNT) {
                    AniPickAnimeCardSkeleton(cardWidth = layout.cardWidth)
                }
            } else if (actors.isEmpty() && emptyMessage != null) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AniPickEmptyState(
                        message = emptyMessage,
                        onRetryClick = onRetryClick,
                        modifier = Modifier.fillMaxWidth().height(EMPTY_STATE_HEIGHT),
                    )
                }
            } else {
                items(actors, key = { it.personId }) { actor ->
                    AniPickActorCard(
                        actor = actor,
                        cardWidth = layout.cardWidth,
                        background = AniPickCardBackground.GRAY,
                        onClick = { onActorClick(actor.personId) },
                    )
                }
                if (onLoadMore != null && isLoadingMore) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AniPickLoadMoreIndicator()
                    }
                }
            }
        }
    }
}
