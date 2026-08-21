package com.jparkbro.home.impl.detail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickSortDropdown

/** 공개 예정 정렬 기준. 서버 sort 파라미터 값과 화면 표시 문구를 묶는다. */
private val comingSoonSortOptions = listOf(
    "latest" to "최신순",
    "popularity" to "인기순",
    "startDate" to "방영 예정 순",
)

/** 공개 예정 화면 헤더 - 정렬 기준 드롭다운. [selectedSort]가 null이면 첫 번째 옵션(최신순)을 기본 표시로 쓴다. */
@Composable
internal fun ComingSoonSortHeader(
    selectedSort: String?,
    onSortSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val effectiveSort = selectedSort ?: comingSoonSortOptions.first().first

    AniPickSortDropdown(
        options = comingSoonSortOptions,
        selected = effectiveSort,
        onSortSelected = onSortSelected,
        modifier = modifier.padding(top = 12.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun ComingSoonSortHeaderPreview() {
    ComingSoonSortHeader(
        selectedSort = "popularity",
        onSortSelected = {},
    )
}
