package com.jparkbro.search.impl.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickCountLabel
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.studio.Studio
import com.jparkbro.core.ui.effect.LoadMoreEffect

private const val SEARCH_STUDIO_LIST_SKELETON_ITEM_COUNT = 8

@Composable
internal fun SearchStudioList(
    studios: List<Studio>,
    isLoading: Boolean,
    totalCount: Int,
    onStudioClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isLoadingMore: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
    emptyMessage: String? = null,
    onRetryClick: (() -> Unit)? = null,
) {
    val listState = rememberLazyListState()

    if (onLoadMore != null) {
        LoadMoreEffect(state = listState, onLoadMore = onLoadMore)
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = !isLoading,
    ) {
        item {
            AniPickCountLabel(count = totalCount, unit = "개")
        }

        if (isLoading) {
            items(SEARCH_STUDIO_LIST_SKELETON_ITEM_COUNT) {
                SearchStudioRowSkeleton()
            }
        } else if (studios.isEmpty() && emptyMessage != null) {
            item {
                AniPickEmptyState(
                    message = emptyMessage,
                    onRetryClick = onRetryClick,
                    modifier = Modifier.fillParentMaxSize(),
                )
            }
        } else {
            items(studios, key = { it.studioId }) { studio ->
                SearchStudioRow(
                    studio = studio,
                    onClick = { onStudioClick(studio.studioId) },
                )
            }
            if (onLoadMore != null && isLoadingMore) {
                item { AniPickLoadMoreIndicator() }
            }
        }
    }
}

@Composable
private fun SearchStudioRow(studio: Studio, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = studio.name ?: "-",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
        )
        Icon(
            imageVector = ChevronRight,
            contentDescription = null,
            tint = AniPickTheme.colors.black,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun SearchStudioRowSkeleton() {
    AniPickShimmerBox(modifier = Modifier.width(160.dp).height(16.dp))
}
