package com.jparkbro.search.impl.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jparkbro.core.designsystem.component.AniPickPrimaryTabRow
import com.jparkbro.core.designsystem.model.AniPickTabItem
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
    AniPickPrimaryTabRow(
        tabs = listOf(
            AniPickTabItem(label = "작품", subLabel = "${animeCount}건"),
            AniPickTabItem(label = "인물", subLabel = "${actorCount}건"),
            AniPickTabItem(label = "제작사", subLabel = "${studioCount}건"),
        ),
        selectedIndex = SearchType.entries.indexOf(searchType),
        onTabClick = { index -> onTabClick(SearchType.entries[index]) },
        modifier = modifier,
    )
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
