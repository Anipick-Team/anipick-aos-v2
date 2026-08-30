package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.catalog.impl.anime.CatalogAnimeAction
import com.jparkbro.catalog.impl.anime.CatalogAnimeState
import com.jparkbro.catalog.impl.anime.CatalogAnimeTab
import com.jparkbro.core.designsystem.component.AniPickSecondaryTabRow
import com.jparkbro.core.designsystem.model.AniPickTabItem
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 축소 상태 헤더 바(조건부) + 탭 - CatalogAnimeScreen LazyColumn의 stickyHeader. */
@Composable
internal fun AnimeStickyTabHeader(
    state: CatalogAnimeState,
    onAction: (CatalogAnimeAction) -> Unit,
    isTopBarCollapsed: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(AniPickTheme.colors.white)) {
        if (isTopBarCollapsed) {
            AnimeCollapsedHeaderBar(
                title = state.animeDetail.title ?: "-",
                isLiked = state.animeDetail.isLiked ?: false,
                onBackClick = { onAction(CatalogAnimeAction.OnBackClick) },
                onLikeClick = { onAction(CatalogAnimeAction.OnLikeClick) },
            )
        }
        AniPickSecondaryTabRow(
            tabs = listOf(
                AniPickTabItem(label = "작품 정보"),
                AniPickTabItem(label = "리뷰", subLabel = "(${state.animeDetail.reviewCount ?: 0})"),
                AniPickTabItem(label = "커뮤니티"),
            ),
            selectedIndex = state.selectedTab.ordinal,
            onTabClick = { index -> onAction(CatalogAnimeAction.OnTabChanged(CatalogAnimeTab.entries[index])) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            isTabEnabled = { index -> !state.isLoading || index == CatalogAnimeTab.INFO.ordinal },
        )
    }
}
