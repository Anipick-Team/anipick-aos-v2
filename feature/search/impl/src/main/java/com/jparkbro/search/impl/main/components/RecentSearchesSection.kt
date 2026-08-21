package com.jparkbro.search.impl.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickRemovableChip
import com.jparkbro.core.designsystem.theme.AniPickTheme

internal fun LazyGridScope.recentSearchesSection(
    recentSearches: List<String>,
    recentSearchesState: LazyListState,
    onClearAllClick: () -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchRemoveClick: (String) -> Unit,
) {
    if (recentSearches.isEmpty()) return

    item(span = { GridItemSpan(maxLineSpan) }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "최근 검색어",
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
            Text(
                text = "전체 삭제",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
                modifier = Modifier.clickable(onClick = onClearAllClick),
            )
        }
    }
    item(span = { GridItemSpan(maxLineSpan) }) {
        LaunchedEffect(recentSearches) {
            recentSearchesState.scrollToItem(0)
        }
        LazyRow(
            state = recentSearchesState,
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentSearches, key = { it }) { query ->
                RecentSearchChip(
                    query = query,
                    onClick = { onRecentSearchClick(query) },
                    onRemoveClick = { onRecentSearchRemoveClick(query) },
                )
            }
        }
    }
}

@Composable
private fun RecentSearchChip(
    query: String,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    AniPickRemovableChip(
        text = query,
        onRemove = onRemoveClick,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.Transparent,
        contentColor = AniPickTheme.colors.black,
        borderColor = AniPickTheme.colors.gray,
        removeContentDescription = "$query 최근 검색어 삭제",
    )
}
