package com.jparkbro.catalog.impl.series

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.catalog.impl.series.components.CatalogSeriesHeader
import com.jparkbro.core.designsystem.component.AniPickCountLabel
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CatalogSeriesRoot(
    animeId: Long,
    animeTitle: String,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: CatalogSeriesViewModel = koinViewModel(parameters = { parametersOf(animeId, animeTitle) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogSeriesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogSeriesAction.Navigation -> when (action) {
                    CatalogSeriesAction.OnBackClick -> onBackClick()
                    is CatalogSeriesAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CatalogSeriesScreen(
    state: CatalogSeriesState,
    onAction: (CatalogSeriesAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "시리즈 정보",
                onBackClick = { onAction(CatalogSeriesAction.OnBackClick) },
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
                onAnimeClick = { anime -> anime.animeId?.let { onAction(CatalogSeriesAction.OnAnimeClick(it)) } },
                modifier = Modifier.padding(innerPadding),
                onLoadMore = { onAction(CatalogSeriesAction.OnLoadMore) },
                header = {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        CatalogSeriesHeader(animeTitle = state.animeTitle)
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AniPickCountLabel(
                            count = state.count,
                            unit = "개",
                            contentPadding = PaddingValues(0.dp),
                        )
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
private fun CatalogSeriesScreenPreview() {
    CatalogSeriesScreen(
        state = CatalogSeriesState(
            animeId = 1L,
            animeTitle = "마법사의 신부",
            count = previewAnimes.size,
            animes = previewAnimes,
        ),
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogSeriesScreenSkeletonPreview() {
    CatalogSeriesScreen(
        state = CatalogSeriesState(animeId = 1L, isLoading = true),
        onAction = {},
    )
}
