package com.jparkbro.auth.impl.preferencesetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupAction
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupState
import com.jparkbro.core.designsystem.component.AniPickFilterChip
import com.jparkbro.core.designsystem.component.AniPickSearchTextField
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.metadata.FilterType

@Composable
internal fun PreferenceSetupSearchFilterSection(
    state: PreferenceSetupState,
    onAction: (PreferenceSetupAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "진행한 평가 수",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
            )

            Text(
                text = state.ratedCount.toString(),
                style = AniPickTheme.typography.body1,
                color = AniPickTheme.colors.primary,
            )
        }
        AniPickSearchTextField(
            state = state.searchFieldState,
            onSearchClick = { onAction(PreferenceSetupAction.OnSearchClick) },
            onClearClick = { onAction(PreferenceSetupAction.OnSearchClearClick) },
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AniPickFilterChip(
                text = state.selectedYear?.let { "${it}년" } ?: "년도",
                isSelected = state.selectedYear != null,
                isExpanded = state.activeFilterSheet == FilterType.YEAR,
                onClick = { onAction(PreferenceSetupAction.OnFilterChipClick(FilterType.YEAR)) }
            )
            AniPickFilterChip(
                text = state.selectedSeason?.name ?: "분기",
                isSelected = state.selectedSeason != null,
                isExpanded = state.activeFilterSheet == FilterType.SEASON,
                onClick = { onAction(PreferenceSetupAction.OnFilterChipClick(FilterType.SEASON)) }
            )
            AniPickFilterChip(
                text = state.selectedGenre?.name ?: "장르",
                isSelected = state.selectedGenre != null,
                isExpanded = state.activeFilterSheet == FilterType.GENRE,
                onClick = { onAction(PreferenceSetupAction.OnFilterChipClick(FilterType.GENRE)) }
            )
        }
    }
}
