package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.catalog.impl.anime.CatalogAnimeAction
import com.jparkbro.catalog.impl.anime.CatalogAnimeState
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme

private val WATCH_STATUSES = listOf(
    "WATCHLIST" to "볼 애니",
    "WATCHING" to "보는 중",
    "FINISHED" to "다 본 애니",
)

/** 히어로 배너 + 타이틀 + 시청상태 버튼 - CatalogAnimeScreen LazyColumn의 첫 item. */
internal fun LazyListScope.animeHeroSection(
    state: CatalogAnimeState,
    onAction: (CatalogAnimeAction) -> Unit,
) {
    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (state.isLoading) {
                AnimeHeroBannerSkeleton(
                    onBackClick = { onAction(CatalogAnimeAction.OnBackClick) },
                )
                AnimeTitleSectionSkeleton(
                    onShareClick = { onAction(CatalogAnimeAction.OnShareClick) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            } else {
                AnimeHeroBanner(
                    title = state.animeDetail.title ?: "-",
                    bannerImageUrl = state.animeDetail.bannerImageUrl,
                    coverImageUrl = state.animeDetail.coverImageUrl,
                    onBackClick = { onAction(CatalogAnimeAction.OnBackClick) },
                    onBannerClick = { onAction(CatalogAnimeAction.OnBannerImageClick) },
                    onCoverClick = { onAction(CatalogAnimeAction.OnCoverImageClick) },
                )
                AnimeTitleSection(
                    title = state.animeDetail.title ?: "-",
                    averageRating = state.animeDetail.averageRating ?: "",
                    isLiked = state.animeDetail.isLiked ?: false,
                    onLikeClick = { onAction(CatalogAnimeAction.OnLikeClick) },
                    onShareClick = { onAction(CatalogAnimeAction.OnShareClick) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                WATCH_STATUSES.forEach { (status, label) ->
                    val isSelected = state.animeDetail.watchStatus == status
                    AniPickButton(
                        text = label,
                        onClick = { onAction(CatalogAnimeAction.OnWatchStatusClick(status)) },
                        modifier = Modifier.weight(1f),
                        size = ButtonSize.S,
                        backgroundColor = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.backgroundGray,
                        contentColor = if (isSelected) AniPickTheme.colors.white else AniPickTheme.colors.black,
                    )
                }
            }
            AniPickSectionDivider(
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}
