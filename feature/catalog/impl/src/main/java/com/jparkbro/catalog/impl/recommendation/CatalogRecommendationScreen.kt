package com.jparkbro.catalog.impl.recommendation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.catalog.impl.recommendation.components.CatalogRecommendationHeader
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.extension.modifier.BottomEdgeShadowClearance
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CatalogRecommendationRoot(
    basedOnAnimeId: Long,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: CatalogRecommendationViewModel = koinViewModel(parameters = { parametersOf(basedOnAnimeId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogRecommendationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogRecommendationAction.Navigation -> when (action) {
                    CatalogRecommendationAction.OnBackClick -> onBackClick()
                    is CatalogRecommendationAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CatalogRecommendationScreen(
    state: CatalogRecommendationState,
    onAction: (CatalogRecommendationAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "함께 볼만한 작품",
                onBackClick = { onAction(CatalogRecommendationAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        if (state.isLoading) {
            AniPickAnimeGridSkeleton(
                modifier = Modifier.padding(innerPadding),
                itemCount = 18,
            )
        } else {
            AniPickAnimeInfiniteGrid(
                animes = state.animes,
                onAnimeClick = { anime -> anime.animeId?.let { onAction(CatalogRecommendationAction.OnAnimeClick(it)) } },
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = BottomEdgeShadowClearance),
                onLoadMore = { onAction(CatalogRecommendationAction.OnLoadMore) },
                header = {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        CatalogRecommendationHeader(referenceAnimeTitle = state.referenceAnimeTitle)
                    }
                },
                footer = if (state.isLoadingMore) {
                    { item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() } }
                } else {
                    null
                },
            )
        }
    }
}

private val previewAnimes = (1..15).map { id ->
    Anime(animeId = id.toLong(), title = "샘플 애니메이션 $id", coverImageUrl = "")
}

@Preview(showBackground = true)
@Composable
private fun CatalogRecommendationScreenPreview() {
    CatalogRecommendationScreen(
        state = CatalogRecommendationState(
            basedOnAnimeId = 1L,
            referenceAnimeTitle = "너에게 닿기를",
            animes = previewAnimes,
        ),
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogRecommendationScreenSkeletonPreview() {
    CatalogRecommendationScreen(
        state = CatalogRecommendationState(basedOnAnimeId = 1L, isLoading = true),
        onAction = {},
    )
}
