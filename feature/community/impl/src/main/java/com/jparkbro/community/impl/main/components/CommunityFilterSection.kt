package com.jparkbro.community.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.community.impl.main.CommunityPostFilter
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickSwitch
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme

private val POST_FILTERS = listOf(
    CommunityPostFilter.ALL to "전체",
    CommunityPostFilter.MONTHLY to "월간",
    CommunityPostFilter.WEEKLY to "주간",
    CommunityPostFilter.DAILY to "일간",
)

/** 게시글 목록 상단 필터 - 기간 필터 버튼 + 스포일러 노출 스위치 */
@Composable
internal fun CommunityFilterSection(
    selectedFilter: CommunityPostFilter,
    isSpoilerVisible: Boolean,
    onFilterClick: (CommunityPostFilter) -> Unit,
    onSpoilerVisibleChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            POST_FILTERS.forEach { (filter, label) ->
                val isSelected = selectedFilter == filter
                AniPickButton(
                    text = label,
                    onClick = { onFilterClick(filter) },
                    modifier = Modifier.weight(1f),
                    size = ButtonSize.S,
                    backgroundColor = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.backgroundGray,
                    contentColor = if (isSelected) AniPickTheme.colors.white else AniPickTheme.colors.black,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "스포일러",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.primary,
                modifier = Modifier.padding(end = 12.dp),
            )
            AniPickSwitch(
                checked = isSpoilerVisible,
                onCheckedChange = onSpoilerVisibleChange,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityFilterSectionPreview() {
    CommunityFilterSection(
        selectedFilter = CommunityPostFilter.ALL,
        isSpoilerVisible = true,
        onFilterClick = {},
        onSpoilerVisibleChange = {},
    )
}
