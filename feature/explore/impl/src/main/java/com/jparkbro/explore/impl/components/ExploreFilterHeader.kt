package com.jparkbro.explore.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickFilterChip
import com.jparkbro.core.designsystem.component.AniPickSearchTextField
import com.jparkbro.core.designsystem.component.AniPickSecondaryTabRow
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.model.AniPickTabItem
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.explore.impl.ExploreAction
import com.jparkbro.explore.impl.ExploreState
import com.jparkbro.explore.impl.ExploreTab
import com.jparkbro.explore.impl.hasActiveFilters

private val EXPLORE_TABS = listOf(
    AniPickTabItem(label = "작품 탐색"),
    AniPickTabItem(label = "커뮤니티"),
)

/** 탐색 화면 상단 헤더 - 작품 탐색/커뮤니티 탭 + (탭별 필터 영역) + 정렬 헤더를 묶는다. */
@Composable
internal fun ExploreFilterHeader(
    state: ExploreState,
    onAction: (ExploreAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AniPickSecondaryTabRow(
            tabs = EXPLORE_TABS,
            selectedIndex = state.tab.ordinal,
            onTabClick = { index -> onAction(ExploreAction.OnTabSelected(ExploreTab.entries[index])) },
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
        )
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (state.tab) {
                ExploreTab.ANIME -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        AniPickFilterChip(
                            text = "년도/분기",
                            isSelected = state.year != null,
                            isExpanded = state.activeFilterSheet == FilterType.YEAR,
                            onClick = { onAction(ExploreAction.OnFilterChipClick(FilterType.YEAR)) },
                        )
                        AniPickFilterChip(
                            text = "장르",
                            isSelected = state.genres.isNotEmpty(),
                            isExpanded = state.activeFilterSheet == FilterType.GENRE,
                            onClick = { onAction(ExploreAction.OnFilterChipClick(FilterType.GENRE)) },
                        )
                        AniPickFilterChip(
                            text = "타입",
                            isSelected = state.type != null,
                            isExpanded = state.activeFilterSheet == FilterType.TYPE,
                            onClick = { onAction(ExploreAction.OnFilterChipClick(FilterType.TYPE)) },
                        )
                    }
                    AniPickSectionDivider()
                    if (state.hasActiveFilters) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            state.year?.let { year ->
                                item(key = "year") {
                                    RemovableFilterChip(
                                        text = "$year",
                                        onRemove = { onAction(ExploreAction.OnYearFilterRemove) },
                                    )
                                }
                            }
                            state.season?.let { season ->
                                item(key = "season") {
                                    RemovableFilterChip(
                                        text = season.name ?: "-",
                                        onRemove = { onAction(ExploreAction.OnSeasonFilterRemove) },
                                    )
                                }
                            }
                            items(state.genres, key = { it.id }) { genre ->
                                RemovableFilterChip(
                                    text = genre.name ?: "-",
                                    onRemove = { onAction(ExploreAction.OnGenreFilterRemove(genre)) },
                                )
                            }
                            state.type?.let { type ->
                                item(key = "type") {
                                    RemovableFilterChip(
                                        text = type,
                                        onRemove = { onAction(ExploreAction.OnTypeFilterRemove) },
                                    )
                                }
                            }
                        }
                        AniPickSectionDivider()
                    }
                }

                ExploreTab.COMMUNITY -> {
                    AniPickSearchTextField(
                        state = state.communitySearchState,
                        onSearchClick = { onAction(ExploreAction.OnCommunitySearchClick) },
                        onClearClick = { onAction(ExploreAction.OnCommunitySearchClearClick) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    )
                    AniPickSectionDivider()
                }
            }
            ExploreSortHeader(
                tab = state.tab,
                sort = if (state.tab == ExploreTab.COMMUNITY) state.communitySort else state.sort,
                onSortSelected = { sort -> onAction(ExploreAction.OnSortSelected(sort)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreFilterHeaderAnimeTabPreview() {
    ExploreFilterHeader(
        state = ExploreState(
            tab = ExploreTab.ANIME,
            year = 2025,
            genres = listOf(Genre(id = 1, name = "액션")),
        ),
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ExploreFilterHeaderCommunityTabPreview() {
    ExploreFilterHeader(
        state = ExploreState(tab = ExploreTab.COMMUNITY),
        onAction = {},
    )
}
