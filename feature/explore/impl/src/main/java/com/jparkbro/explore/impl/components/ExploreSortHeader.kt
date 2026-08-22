package com.jparkbro.explore.impl.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jparkbro.core.designsystem.component.AniPickSortDropdown
import com.jparkbro.explore.impl.ExploreSort
import com.jparkbro.explore.impl.ExploreTab

/** 작품 탐색 탭 정렬 기준 표시 문구. */
private val animeSortOptions = listOf(
    ExploreSort.POPULARITY to "인기순",
    ExploreSort.RATING to "평점순",
)

/** 커뮤니티 탭 정렬 기준 표시 문구 - 평점순 대신 최신순을 쓴다. */
private val communitySortOptions = listOf(
    ExploreSort.POPULARITY to "인기순",
    ExploreSort.LATEST to "최신순",
)

/** 탐색 화면 헤더 - 오른쪽 끝에 현재 정렬 기준을 보여주는 드롭다운 트리거를 둔다. [tab]에 따라 선택 가능한 옵션이 다르다. */
@Composable
internal fun ExploreSortHeader(
    tab: ExploreTab,
    sort: ExploreSort,
    onSortSelected: (ExploreSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    AniPickSortDropdown(
        options = if (tab == ExploreTab.COMMUNITY) communitySortOptions else animeSortOptions,
        selected = sort,
        onSortSelected = onSortSelected,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun ExploreSortHeaderAnimeTabPreview() {
    ExploreSortHeader(
        tab = ExploreTab.ANIME,
        sort = ExploreSort.POPULARITY,
        onSortSelected = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ExploreSortHeaderCommunityTabPreview() {
    ExploreSortHeader(
        tab = ExploreTab.COMMUNITY,
        sort = ExploreSort.POPULARITY,
        onSortSelected = {},
    )
}
