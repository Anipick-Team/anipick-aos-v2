package com.jparkbro.catalog.impl.studio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.extension.modifier.fullBleedHorizontal
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeCard
import com.jparkbro.core.ui.component.AniPickCardBackground
import com.jparkbro.core.ui.effect.LoadMoreEffect
import com.jparkbro.core.ui.component.calculateAnimeGridLayout

private val STUDIO_GRID_HORIZONTAL_PADDING = 20.dp
private const val UNKNOWN_YEAR_LABEL = "연도 미상"

/** 제작사 작품 목록 - [Anime.seasonYear] 기준 연도별 섹션 그리드. 연도 없는 작품은 [UNKNOWN_YEAR_LABEL]로 묶어 맨 뒤에 배치 */
@Composable
internal fun StudioAnimeSectionGrid(
    animes: List<Anime>,
    isLoadingMore: Boolean,
    onAnimeClick: (Anime) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sections = remember(animes) {
        animes.groupBy { it.seasonYear }
            .toList()
            .sortedByDescending { (year, _) -> year?.toIntOrNull() ?: Int.MIN_VALUE }
    }
    val gridState = rememberLazyGridState()

    BoxWithConstraints(modifier = modifier) {
        val layout = remember(maxWidth) {
            calculateAnimeGridLayout(availableWidth = maxWidth - STUDIO_GRID_HORIZONTAL_PADDING * 2)
        }

        LoadMoreEffect(state = gridState, threshold = layout.columns + 1, onLoadMore = onLoadMore)

        LazyVerticalGrid(
            columns = GridCells.Fixed(layout.columns),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = STUDIO_GRID_HORIZONTAL_PADDING, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(layout.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            sections.forEachIndexed { index, (year, yearAnimes) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box {
                        Text(
                            text = year?.ifBlank { UNKNOWN_YEAR_LABEL } ?: UNKNOWN_YEAR_LABEL,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.black,
                            modifier = Modifier
                                .background(AniPickTheme.colors.lightGray, CircleShape)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                items(yearAnimes, key = { it.animeId ?: it.hashCode() }) { anime ->
                    AniPickAnimeCard(
                        anime = anime,
                        cardWidth = layout.cardWidth,
                        background = AniPickCardBackground.GRAY,
                        onClick = { onAnimeClick(anime) },
                    )
                }
                if (index != sections.size - 1) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AniPickSectionDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fullBleedHorizontal(STUDIO_GRID_HORIZONTAL_PADDING),
                        )
                    }
                }
            }
            if (isLoadingMore) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AniPickLoadMoreIndicator()
                }
            }
        }
    }
}
