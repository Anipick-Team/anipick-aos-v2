package com.jparkbro.search.impl.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.ui.AniPickAnimeCardSkeleton
import com.jparkbro.core.ui.calculateAnimeGridLayout
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

private const val SEARCH_ACTOR_GRID_SKELETON_ITEM_COUNT = 18
private val SEARCH_ACTOR_GRID_HORIZONTAL_PADDING = 20.dp
private val SEARCH_ACTOR_GRID_VERTICAL_SPACING = 16.dp

private const val ACTOR_CARD_ASPECT_RATIO = 128f / 182f

@Composable
internal fun SearchActorList(
    actors: List<Actor>,
    isLoading: Boolean,
    totalCount: Int,
    onActorClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isLoadingMore: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
) {
    BoxWithConstraints(modifier = modifier) {
        val layout = remember(maxWidth) {
            calculateAnimeGridLayout(availableWidth = maxWidth - SEARCH_ACTOR_GRID_HORIZONTAL_PADDING * 2)
        }
        val gridState = rememberLazyGridState()

        if (onLoadMore != null) {
            val threshold = layout.columns + 1
            val shouldLoadMore by remember(threshold) {
                derivedStateOf {
                    val layoutInfo = gridState.layoutInfo
                    val totalItemsCount = layoutInfo.totalItemsCount
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - threshold
                }
            }

            LaunchedEffect(gridState, threshold) {
                snapshotFlow { shouldLoadMore }
                    .distinctUntilChanged()
                    .filter { it }
                    .collect { onLoadMore() }
            }
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
                SearchResultCountHeader(count = totalCount, unit = "명")
            }

            if (isLoading) {
                items(SEARCH_ACTOR_GRID_SKELETON_ITEM_COUNT) {
                    AniPickAnimeCardSkeleton(cardWidth = layout.cardWidth)
                }
            } else {
                items(actors, key = { it.personId }) { actor ->
                    SearchActorCard(
                        actor = actor,
                        cardWidth = layout.cardWidth,
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

@Composable
private fun SearchActorCard(actor: Actor, cardWidth: Dp, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(cardWidth)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = actor.profileImage.ifBlank { null },
            contentDescription = actor.name,
            error = painterResource(R.drawable.profile_default_img),
            placeholder = painterResource(R.drawable.profile_default_img),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ACTOR_CARD_ASPECT_RATIO)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = actor.name,
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
