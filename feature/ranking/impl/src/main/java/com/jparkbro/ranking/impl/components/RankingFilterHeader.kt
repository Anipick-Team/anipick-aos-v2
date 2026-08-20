package com.jparkbro.ranking.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickFilterChip
import com.jparkbro.core.designsystem.component.AniPickPillChip
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.ranking.impl.RankingAction
import com.jparkbro.ranking.impl.RankingState
import com.jparkbro.ranking.impl.RankingType
import com.jparkbro.ranking.impl.yearSeasonLabel

/** 랭킹 화면 상단 고정 헤더 - 랭킹 타입(실시간/시즌/역대) 필터 알약 + 장르 필터 칩 + 구분선. */
@Composable
internal fun RankingFilterHeader(
    state: RankingState,
    onAction: (RankingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickTheme.colors.white)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AniPickPillChip(
                    text = "실시간",
                    isSelected = state.rankingType == RankingType.REAL_TIME,
                    onClick = { onAction(RankingAction.OnRankingTypeSelected(RankingType.REAL_TIME)) },
                )
                AniPickPillChip(
                    text = state.yearSeasonLabel,
                    isSelected = state.rankingType == RankingType.YEAR_SEASON,
                    onClick = { onAction(RankingAction.OnFilterChipClick(FilterType.YEAR)) },
                )
                AniPickPillChip(
                    text = "역대",
                    isSelected = state.rankingType == RankingType.ALL_TIME,
                    onClick = { onAction(RankingAction.OnRankingTypeSelected(RankingType.ALL_TIME)) },
                )
            }
            AniPickFilterChip(
                text = state.genre?.name ?: "장르",
                isSelected = state.genre != null,
                isExpanded = state.activeFilterSheet == FilterType.GENRE,
                onClick = { onAction(RankingAction.OnFilterChipClick(FilterType.GENRE)) },
            )
        }
        AniPickSectionDivider()
    }
}
