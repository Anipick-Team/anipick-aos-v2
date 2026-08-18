package com.jparkbro.search.impl.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.search.impl.detail.SearchType

@Composable
internal fun SearchTypeTabRow(
    searchType: SearchType,
    animeCount: Int,
    actorCount: Int,
    studioCount: Int,
    onTabClick: (SearchType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var tabBounds by remember { mutableStateOf(emptyMap<SearchType, Pair<Dp, Dp>>()) }

    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SearchType.entries.forEach { type ->
                val isSelected = searchType == type
                Row(
                    modifier = Modifier
                        .clickable { onTabClick(type) }
                        .padding(top = 12.dp, bottom = 16.dp)
                        .onGloballyPositioned { coordinates ->
                            val offset = with(density) { coordinates.positionInParent().x.toDp() }
                            val width = with(density) { coordinates.size.width.toDp() }
                            tabBounds = tabBounds + (type to (offset to width))
                        },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = when (type) {
                            SearchType.ANIME -> "작품"
                            SearchType.ACTOR -> "인물"
                            SearchType.STUDIO -> "제작사"
                        },
                        style = AniPickTheme.typography.body2,
                        color = if (isSelected) AniPickTheme.colors.black else AniPickTheme.colors.textGray,
                    )
                    Text(
                        text = "${
                            when (type) {
                                SearchType.ANIME -> animeCount
                                SearchType.ACTOR -> actorCount
                                SearchType.STUDIO -> studioCount
                            }
                        }건",
                        style = AniPickTheme.typography.caption1,
                        color = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.textGray,
                    )
                }
            }
        }

        val (targetOffset, targetWidth) = tabBounds[searchType] ?: (0.dp to 0.dp)
        val indicatorOffset by animateDpAsState(targetValue = targetOffset, label = "tabIndicatorOffset")
        val indicatorWidth by animateDpAsState(targetValue = targetWidth, label = "tabIndicatorWidth")

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = indicatorOffset)
                .width(indicatorWidth.coerceAtLeast(0.dp)),
            thickness = 2.dp,
            color = AniPickTheme.colors.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTypeTabRowPreview() {
    SearchTypeTabRow(
        searchType = SearchType.ANIME,
        animeCount = 12,
        actorCount = 3,
        studioCount = 1,
        onTabClick = {},
    )
}
